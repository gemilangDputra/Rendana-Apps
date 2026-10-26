package com.gdproduct.rendana.data

import android.content.Context
import com.gdproduct.rendana.data.local.entity.TransactionEntity
import com.gdproduct.rendana.data.repository.TransactionCategoryRepository
import com.gdproduct.rendana.data.repository.TransactionRepository
import com.gdproduct.rendana.domain.model.TransactionType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random
import kotlinx.coroutines.flow.first

suspend fun generateTestTransactions(
    context: Context,
    transactionRepository: TransactionRepository,
    categoryRepository: TransactionCategoryRepository
) {
    val preferences = context.getSharedPreferences(
        "rendana_test_generator",
        Context.MODE_PRIVATE
    )

    // Jangan membuat kumpulan data yang sama dua kali.
    if (preferences.getBoolean("generated", false)) return

    val expenseCategories = categoryRepository
        .getByType(TransactionType.EXPENSE.name)
        .first()

    val incomeCategories = categoryRepository
        .getByType(TransactionType.INCOME.name)
        .first()

    require(expenseCategories.isNotEmpty()) {
        "Kategori pengeluaran belum tersedia."
    }
    require(incomeCategories.isNotEmpty()) {
        "Kategori penghasilan belum tersedia."
    }

    val random = Random.Default
    val startDate = LocalDate.of(2025, 1, 1)
    val endDate = minOf(
        LocalDate.of(2026, 10, 31),
        LocalDate.now()
    )

    val expenseNames = listOf(
        "Makan siang", "Sarapan", "Makan malam",
        "Beli kebutuhan rumah", "Transportasi",
        "Beli minuman", "Belanja bulanan",
        "Bayar internet", "Beli perlengkapan",
        "Jajan", "Bayar parkir", "Beli obat"
    )

    val incomeNames = listOf(
        "Gaji bulanan", "Pendapatan tambahan",
        "Bonus", "Uang proyek", "Pendapatan usaha"
    )

    val merchants = listOf(
        "Warung Makan", "Minimarket", "Supermarket",
        "Toko Kelontong", "Kedai Kopi", "Toko Online",
        "Transportasi Umum", "Pembayaran Digital"
    )

    var date = startDate

    while (!date.isAfter(endDate)) {
        val dailyCount = random.nextInt(5, 11)

        repeat(dailyCount) {
            val isIncome = random.nextInt(100) < 20
            val type = if (isIncome) {
                TransactionType.INCOME
            } else {
                TransactionType.EXPENSE
            }

            val categories = if (isIncome) {
                incomeCategories
            } else {
                expenseCategories
            }

            val name = if (isIncome) {
                incomeNames.random(random)
            } else {
                expenseNames.random(random)
            }

            // Nominal rupiah dengan variasi yang relatif wajar.
            val amount = if (isIncome) {
                random.nextLong(500_000L, 5_000_001L)
            } else {
                random.nextLong(5_000L, 250_001L)
            }

            val transaction = TransactionEntity(
                merchantName = merchants.random(random),
                name = name,
                amount = amount,
                type = type.name,
                categoryId = categories.random(random).id,
                date = date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                note = null
            )

            transactionRepository.insertTransaction(transaction)
        }

        date = date.plusDays(1)
    }

    preferences.edit()
        .putBoolean("generated", true)
        .apply()
}
