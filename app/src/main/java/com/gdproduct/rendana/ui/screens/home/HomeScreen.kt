package com.gdproduct.rendana.ui.screens.home

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gdproduct.rendana.data.local.RendanaDatabase
import com.gdproduct.rendana.data.repository.TransactionCategoryRepository
import com.gdproduct.rendana.ui.components.TransactionCard
import com.gdproduct.rendana.ui.icons.CalendarClock
import com.gdproduct.rendana.ui.icons.Notifications
import com.gdproduct.rendana.ui.icons.Person
import com.gdproduct.rendana.ui.icons.Receipt
import com.gdproduct.rendana.ui.icons.Savings
import com.gdproduct.rendana.ui.icons.getIcon
import com.gdproduct.rendana.ui.theme.RendanaThemeColors
import com.gdproduct.rendana.utils.GeminiReceiptParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val transactions by homeViewModel.transactions.collectAsState()

    val categoryRepository = remember(context) {
        TransactionCategoryRepository(
            RendanaDatabase.getDatabase(context)
                .transactionCategoryDao()
        )
    }

    val categories by categoryRepository.getAll()
        .collectAsState(initial = emptyList())

    val expenseCategories = remember(categories) {
        categories
            .filter { it.type == "EXPENSE" }
            .map { it.name }
            .distinct()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header Home
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(RendanaThemeColors.primary)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = RendanaThemeColors.primaryDark,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Person,
                        contentDescription = "Profil",
                        modifier = Modifier.size(26.dp),
                        tint = RendanaThemeColors.iconOnPrimary
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = "Hello Gemilang!",
                        color = RendanaThemeColors.textOnPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Icon(
                    imageVector = Notifications,
                    contentDescription = "Notifikasi",
                    modifier = Modifier.size(27.dp),
                    tint = RendanaThemeColors.iconOnPrimary
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HomeQuickMenu(
                    icon = {
                        Icon(
                            imageVector = Savings,
                            contentDescription = null
                        )
                    },
                    label = "Target",
                    modifier = Modifier.weight(1f)
                )

                HomeQuickMenu(
                    icon = {
                        Icon(
                            imageVector = CalendarClock,
                            contentDescription = null
                        )
                    },
                    label = "Pengingat",
                    modifier = Modifier.weight(1f)
                )

                HomeQuickMenu(
                    icon = {
                        Icon(
                            imageVector = Receipt,
                            contentDescription = null
                        )
                    },
                    label = "Transaksi",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Konten Home
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ReceiptScanTestSection(
                context = context,
                categories = expenseCategories
            )

            Text(
                text = "Transaksi terbaru",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = RendanaThemeColors.textPrimary
            )

            if (transactions.isEmpty()) {
                Text(
                    text = "Belum ada transaksi. Yuk, catat transaksi pertamamu!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RendanaThemeColors.textSecondary
                )
            } else {
                transactions.forEach { transaction ->
                    TransactionCard(
                        title = transaction.title,
                        amount = transaction.amount,
                        type = transaction.type,
                        category = transaction.category,
                        date = transaction.date,
                        icon = getIcon(transaction.iconName),
                        merchant = transaction.merchant,
                        note = transaction.note,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun ReceiptScanTestSection(
    context: Context,
    categories: List<String>
) {
    val scope = rememberCoroutineScope()

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var isProcessing by remember {
        mutableStateOf(false)
    }

    var jsonResult by remember {
        mutableStateOf<String?>(null)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    // Pilih gambar dari galeri
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            jsonResult = null
            errorMessage = null
        }
    }

    // Ambil foto melalui aplikasi kamera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            scope.launch {
                try {
                    val uri = withContext(Dispatchers.IO) {
                        saveBitmapToCache(context, bitmap)
                    }

                    selectedImageUri = uri
                    jsonResult = null
                    errorMessage = null
                } catch (exception: Exception) {
                    errorMessage =
                        "Gagal menyimpan foto: ${exception.message}"
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Gemini",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Pilih foto struk. Gemini akan menganalisis gambar secara langsung tanpa OCR terpisah.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        galleryLauncher.launch("image/*")
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isProcessing
                ) {
                    Text("Pilih gambar")
                }

                OutlinedButton(
                    onClick = {
                        cameraLauncher.launch(null)
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isProcessing
                ) {
                    Text("Kamera")
                }
            }

            if (selectedImageUri != null) {
                Text(
                    text = "Gambar siap diproses",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Button(
                onClick = {
                    val uri = selectedImageUri ?: return@Button

                    scope.launch {
                        isProcessing = true
                        jsonResult = null
                        errorMessage = null

                        try {
                            // Langsung kirim gambar ke Gemini.
                            val result = GeminiReceiptParser(context)
                                .parseReceipt(
                                    imageUri = uri,
                                    availableCategories = categories
                                )

                            jsonResult = result.rawJson

                        } catch (exception: Exception) {
                            errorMessage = exception.message
                                ?: "Terjadi kesalahan saat memproses gambar."
                        } finally {
                            isProcessing = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedImageUri != null && !isProcessing
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.size(8.dp))
                    Text("Menganalisis gambar...")
                } else {
                    Text("Proses Struk")
                }
            }

            if (isProcessing) {
                Text(
                    text = "Gemini sedang membaca gambar dan mengekstrak data struk. Mohon tunggu.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            errorMessage?.let { message ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            jsonResult?.let { json ->
                Text(
                    text = "Hasil JSON Gemini",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    SelectionContainer {
                        Text(
                            text = json,
                            modifier = Modifier.padding(12.dp),
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

private fun saveBitmapToCache(
    context: Context,
    bitmap: Bitmap
): Uri {
    val file = File.createTempFile(
        "receipt_scan_",
        ".jpg",
        context.cacheDir
    )

    FileOutputStream(file).use { output ->
        val success = bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            95,
            output
        )

        check(success) {
            "Gagal mengubah foto menjadi file JPEG."
        }
    }

    return Uri.fromFile(file)
}