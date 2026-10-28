package com.gdproduct.rendana.utils

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.gdproduct.rendana.data.local.security.SecureStorage
import com.gdproduct.rendana.ui.screens.profile.ai.AiSettingsViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException

data class ReceiptItem(
    val name: String,
    val quantity: Double?,
    val unitPrice: Long?,
    val category: String?
)

data class ReceiptParseResult(
    val merchantName: String?,
    val date: String?,
    val items: List<ReceiptItem>,
    val rawJson: String
)

class GeminiReceiptParser(context: Context) {

    private val appContext = context.applicationContext
    private val secureStorage = SecureStorage(appContext)
    private val preferences = appContext.getSharedPreferences(
        "ai_settings",
        Context.MODE_PRIVATE
    )

    private data class ModelSettings(
        val primaryModel: String,
        val fallbackModel: String,
        val strategy: String,
        val retryCount: Int
    )

    private class GeminiApiException(
        val statusCode: Int,
        message: String
    ) : IOException(message)

    suspend fun parseReceipt(
        imageUri: Uri,
        availableCategories: List<String>
    ): ReceiptParseResult {
        return withContext(Dispatchers.IO) {
            val apiKey = secureStorage.getApiKey()
                ?.trim()
                ?.takeIf { it.isNotEmpty() }
                ?: throw IllegalStateException(
                    "Gemini API key belum diatur. Silakan isi melalui Pengaturan Kecerdasan Buatan."
                )

            val settings = readSettings()

            val imageBytes = appContext.contentResolver
                .openInputStream(imageUri)
                ?.use { it.readBytes() }
                ?: throw IllegalStateException(
                    "Gambar struk tidak dapat dibaca."
                )

            if (imageBytes.isEmpty()) {
                throw IllegalStateException("File gambar struk kosong.")
            }

            val mimeType = appContext.contentResolver
                .getType(imageUri)
                ?.takeIf {
                    it == "image/jpeg" ||
                        it == "image/png" ||
                        it == "image/webp" ||
                        it == "image/heic" ||
                        it == "image/heif"
                }
                ?: "image/jpeg"

            val imageBase64 = Base64.encodeToString(
                imageBytes,
                Base64.NO_WRAP
            )

            val prompt = buildPrompt(availableCategories)

            suspend fun requestModel(
                modelName: String
            ): ReceiptParseResult {
                return callGemini(
                    modelName = modelName,
                    apiKey = apiKey,
                    imageBase64 = imageBase64,
                    mimeType = mimeType,
                    prompt = prompt
                )
            }

            when (settings.strategy) {
                AiSettingsViewModel.STRATEGY_RETRY -> {
                    retrySameModel(
                        modelName = settings.primaryModel,
                        retryCount = settings.retryCount,
                        request = {
                            requestModel(settings.primaryModel)
                        }
                    )
                }

                AiSettingsViewModel.STRATEGY_FALLBACK -> {
                    try {
                        requestModel(settings.primaryModel)
                    } catch (exception: CancellationException) {
                        throw exception
                    } catch (primaryError: Exception) {
                        if (!canTryFallback(primaryError)) {
                            throw primaryError
                        }

                        if (settings.fallbackModel == settings.primaryModel) {
                            throw IllegalStateException(
                                "Model utama gagal dan model cadangan sama dengan model utama. Periksa Pengaturan Kecerdasan Buatan.",
                                primaryError
                            )
                        }

                        try {
                            requestModel(settings.fallbackModel)
                        } catch (exception: CancellationException) {
                            throw exception
                        } catch (fallbackError: Exception) {
                            throw IllegalStateException(
                                "Model utama dan model cadangan gagal. Model utama: ${settings.primaryModel}. Model cadangan: ${settings.fallbackModel}. Penyebab terakhir: ${fallbackError.message.orEmpty()}",
                                fallbackError
                            )
                        }
                    }
                }

                AiSettingsViewModel.STRATEGY_FEEDBACK -> {
                    try {
                        requestModel(settings.primaryModel)
                    } catch (exception: CancellationException) {
                        throw exception
                    } catch (exception: Exception) {
                        throw IllegalStateException(
                            "Pemrosesan struk gagal menggunakan ${settings.primaryModel}: ${exception.message.orEmpty()}",
                            exception
                        )
                    }
                }

                else -> requestModel(settings.primaryModel)
            }
        }
    }

    private fun readSettings(): ModelSettings {
        val primaryModel = preferences.getString(
            "selected_model",
            "gemini-3.6-flash"
        )?.trim()?.takeIf { it.isNotEmpty() }
            ?: "gemini-3.6-flash"

        val fallbackModel = preferences.getString(
            "selected_fallback_model",
            "gemini-3.8-flash"
        )?.trim()?.takeIf { it.isNotEmpty() }
            ?: "gemini-3.8-flash"

        val strategy = preferences.getString(
            "failure_strategy",
            AiSettingsViewModel.STRATEGY_FALLBACK
        ) ?: AiSettingsViewModel.STRATEGY_FALLBACK

        val retryCount = preferences.getInt(
            "retry_count",
            2
        ).coerceIn(0, 5)

        return ModelSettings(
            primaryModel = primaryModel,
            fallbackModel = fallbackModel,
            strategy = strategy,
            retryCount = retryCount
        )
    }

    private suspend fun retrySameModel(
        modelName: String,
        retryCount: Int,
        request: suspend () -> ReceiptParseResult
    ): ReceiptParseResult {
        var lastError: Exception? = null

        for (attempt in 0..retryCount) {
            try {
                return request()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                lastError = exception

                if (attempt == retryCount || !canRetry(exception)) {
                    break
                }

                val delayMillis =
                    1_000L * (1L shl attempt.coerceAtMost(3))

                delay(delayMillis)
            }
        }

        throw IllegalStateException(
            "Pemrosesan struk gagal menggunakan $modelName setelah percobaan ulang. Penyebab: ${lastError?.message.orEmpty()}",
            lastError
        )
    }

    private fun canRetry(exception: Exception): Boolean {
        return when (exception) {
            is SocketTimeoutException,
            is UnknownHostException -> true

            is GeminiApiException -> {
                exception.statusCode == 408 ||
                    exception.statusCode == 429 ||
                    exception.statusCode in 500..599
            }

            is IOException -> true

            else -> {
                exception !is IllegalArgumentException &&
                    exception !is SecurityException
            }
        }
    }

    private fun canTryFallback(exception: Exception): Boolean {
        return when (exception) {
            is SocketTimeoutException,
            is UnknownHostException -> true

            is GeminiApiException -> {
                exception.statusCode != 401 &&
                    exception.statusCode != 403
            }

            is IOException -> true

            else -> exception !is SecurityException
        }
    }

    private suspend fun callGemini(
        modelName: String,
        apiKey: String,
        imageBase64: String,
        mimeType: String,
        prompt: String
    ): ReceiptParseResult {
        val requestBody = JSONObject().apply {
            put(
                "contents",
                JSONArray().put(
                    JSONObject().apply {
                        put(
                            "parts",
                            JSONArray()
                                .put(
                                    JSONObject().put(
                                        "text",
                                        prompt
                                    )
                                )
                                .put(
                                    JSONObject().put(
                                        "inlineData",
                                        JSONObject().apply {
                                            put("mimeType", mimeType)
                                            put("data", imageBase64)
                                        }
                                    )
                                )
                        )
                    }
                )
            )

            put(
                "generationConfig",
                JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.1)
                    put("maxOutputTokens", 4096)
                }
            )
        }

        val endpoint =
            "https://generativelanguage.googleapis.com/v1beta/" +
                "models/$modelName:generateContent?key=${Uri.encode(apiKey)}"

        val connection =
            (URL(endpoint).openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 20_000
                readTimeout = 60_000
                doOutput = true

                setRequestProperty(
                    "Content-Type",
                    "application/json; charset=UTF-8"
                )
            }

        try {
            connection.outputStream.use { output ->
                output.write(
                    requestBody.toString().toByteArray(Charsets.UTF_8)
                )
            }

            val statusCode = connection.responseCode

            val responseText = (
                if (statusCode in 200..299) {
                    connection.inputStream
                } else {
                    connection.errorStream
                }
            )?.bufferedReader(Charsets.UTF_8)?.use {
                it.readText()
            }.orEmpty()

            if (statusCode !in 200..299) {
                val detail = runCatching {
                    JSONObject(responseText)
                        .optJSONObject("error")
                        ?.optString("message")
                }.getOrNull()

                throw GeminiApiException(
                    statusCode = statusCode,
                    message = "Gemini API gagal (HTTP $statusCode): ${
                        detail?.takeIf { it.isNotBlank() }
                            ?: "Respons tidak diketahui."
                    }"
                )
            }

            val response = JSONObject(responseText)

            val candidates = response.optJSONArray("candidates")
                ?: throw IllegalStateException(
                    "Gemini tidak mengembalikan candidates."
                )

            if (candidates.length() == 0) {
                val blockReason = response
                    .optJSONObject("promptFeedback")
                    ?.optString("blockReason")
                    ?.takeIf { it.isNotBlank() }

                throw IllegalStateException(
                    "Gemini tidak menghasilkan jawaban." +
                        (blockReason?.let { " Alasan: $it." } ?: "")
                )
            }

            val candidate = candidates.getJSONObject(0)

            val finishReason = candidate
                .optString("finishReason")
                .takeIf { it.isNotBlank() }

            if (
                finishReason != null &&
                finishReason != "STOP" &&
                finishReason != "MAX_TOKENS"
            ) {
                throw IllegalStateException(
                    "Jawaban Gemini tidak selesai secara normal: $finishReason"
                )
            }

            val content = candidate.optJSONObject("content")
                ?: throw IllegalStateException(
                    "Konten jawaban Gemini kosong."
                )

            val parts = content.optJSONArray("parts")
                ?: throw IllegalStateException(
                    "Bagian teks jawaban Gemini tidak ditemukan."
                )

            val rawJson = buildString {
                for (index in 0 until parts.length()) {
                    append(
                        parts.optJSONObject(index)
                            ?.optString("text")
                            .orEmpty()
                    )
                }
            }.trim()

            if (rawJson.isBlank()) {
                throw IllegalStateException(
                    "Gemini mengembalikan teks kosong."
                )
            }

            return parseJson(rawJson)
        } finally {
            connection.disconnect()
        }
    }

    private fun buildPrompt(
        availableCategories: List<String>
    ): String {
        val categories = availableCategories
            .distinct()
            .filter { it.isNotBlank() }

        val categoryInstructions = if (categories.isEmpty()) {
            """
            Tidak ada daftar kategori yang diberikan.
            Isi category dengan null untuk setiap barang.
            """.trimIndent()
        } else {
            """
            Kategori yang diperbolehkan:
            ${categories.joinToString("\n") { "- $it" }}

            Pilih category hanya dari daftar tersebut.
            Gunakan ejaan nama kategori persis seperti daftar.
            Jika tidak cocok atau tidak yakin, gunakan null.
            Jangan membuat kategori baru.
            """.trimIndent()
        }

        return """
            Kamu adalah parser struk belanja untuk aplikasi Rendana.

            Analisis gambar struk yang dilampirkan secara langsung.

            Ekstrak nama merchant, tanggal transaksi, dan setiap barang
            yang dibeli berdasarkan informasi yang terlihat pada gambar.

            ATURAN:
                1. Keluarkan hanya satu objek JSON valid.
                2. Jangan keluarkan Markdown atau penjelasan.
                3. Jangan mengarang informasi yang tidak terlihat pada struk.
                4. Informasi yang tidak diketahui harus diisi null jika field mengizinkannya.
                5. Format date adalah YYYY-MM-DD.
                6. Gunakan tanggal yang tercetak pada struk. Jika tidak terlihat atau tidak dapat dipastikan, gunakan null.
                7. quantity adalah jumlah barang yang dibeli. Nilainya boleh desimal untuk barang timbang.
                8. unitPrice adalah harga untuk satu unit barang.
                9. totalPrice adalah total harga untuk satu jenis barang, bukan total pembayaran seluruh struk.
                10. Jangan memasukkan subtotal, pajak umum, biaya layanan, atau total pembayaran seluruh struk sebagai item.
                11. Jangan menggabungkan barang yang berbeda menjadi satu item.
                12. Semua nilai harga harus berupa angka JSON tanpa simbol mata uang atau pemisah ribuan.
                13. Jangan menebak unitPrice jika harga satuan tidak tercetak atau tidak dapat dipastikan.
                14. Abaikan iklan, slogan, dan teks promosi.
                15. Jika tidak ada barang yang dapat diidentifikasi, gunakan array items kosong.
                16. Jika nama merchant tidak terlihat dengan jelas, gunakan null.
                17. Jika total harga suatu barang tercetak jelas pada struk, ekstrak nilainya ke totalPrice.
                18. Jika totalPrice tidak tercetak, tetapi quantity dan unitPrice diketahui dengan pasti, totalPrice boleh dihitung dengan mengalikan quantity × unitPrice.
                19. Jika quantity dan totalPrice diketahui, tetapi unitPrice tidak tercetak, isi unitPrice dengan null. Jangan menghitung hasil pembagian totalPrice ÷ quantity untuk mengisi unitPrice.
                20. Jika quantity tidak diketahui, jangan menganggap nilainya 1 kecuali jumlah satu unit dapat dipastikan dari struk.
                21. Jika informasi untuk menentukan totalPrice tidak cukup, gunakan null. Jangan menebak.
                22. Diskon khusus suatu barang boleh diperhitungkan dalam totalPrice hanya jika nilainya dapat diidentifikasi dengan jelas. Jangan memasukkan diskon umum seluruh struk ke dalam totalPrice barang.
                23. Jika harga satuan atau total harga tidak dapat dibaca dengan pasti, gunakan null untuk field terkait, tanpa mengarang nilainya.

            $categoryInstructions

            STRUKTUR JSON:
            {
              "merchantName": "string atau null",
              "date": "YYYY-MM-DD atau null",
              "items": [
                {
                  "name": "string",
                  "quantity": "number atau null",
                  "unitPrice": "number atau null",
				  "totalPrice": "number atau null",
                  "category": "string atau null"
                }
              ]
            }

            quantity, totalPrice dan unitPrice harus berupa angka JSON atau null,
            bukan string. Gunakan null JSON sungguhan.
        """.trimIndent()
    }

    private fun parseJson(rawJson: String): ReceiptParseResult {
        val root = try {
            JSONObject(rawJson)
        } catch (exception: Exception) {
            throw IllegalStateException(
                "Jawaban Gemini bukan JSON valid.",
                exception
            )
        }

        val itemsJson = root.optJSONArray("items")
            ?: throw IllegalStateException(
                "JSON Gemini tidak memiliki array items."
            )

        val items = buildList {
            for (index in 0 until itemsJson.length()) {
                val item = itemsJson.optJSONObject(index)
                    ?: throw IllegalStateException(
                        "Item pada indeks $index bukan objek JSON."
                    )

                val name = item.optNullableString("name")
                    ?: throw IllegalStateException(
                        "Nama barang pada indeks $index kosong."
                    )

                add(
                    ReceiptItem(
                        name = name,
                        quantity = item.optNullableDouble("quantity"),
                        unitPrice = item.optNullableLong("unitPrice"),
                        category = item.optNullableString("category")
                    )
                )
            }
        }

        val merchantName = root.optNullableString("merchantName")
        val date = root.optNullableString("date")

        if (date != null && !DATE_REGEX.matches(date)) {
            throw IllegalStateException(
                "Format tanggal Gemini tidak valid: $date"
            )
        }

        return ReceiptParseResult(
            merchantName = merchantName,
            date = date,
            items = items,
            rawJson = root.toString(2)
        )
    }

    private fun JSONObject.optNullableString(
        key: String
    ): String? {
        if (!has(key) || isNull(key)) return null

        return optString(key)
            .trim()
            .takeIf { it.isNotBlank() }
    }

    private fun JSONObject.optNullableDouble(
        key: String
    ): Double? {
        if (!has(key) || isNull(key)) return null

        return when (val value = opt(key)) {
            is Number -> value.toDouble()
                .takeIf { it.isFinite() && it >= 0.0 }

            else -> null
        }
    }

    private fun JSONObject.optNullableLong(
        key: String
    ): Long? {
        if (!has(key) || isNull(key)) return null

        return when (val value = opt(key)) {
            is Int -> value.toLong().takeIf { it >= 0L }
            is Long -> value.takeIf { it >= 0L }

            is Double -> value
                .takeIf {
                    it.isFinite() &&
                        it >= 0.0 &&
                        it < Long.MAX_VALUE.toDouble() &&
                        it % 1.0 == 0.0
                }
                ?.toLong()

            else -> null
        }
    }

    private companion object {
        val DATE_REGEX = Regex("""\d{4}-\d{2}-\d{2}""")
    }
}