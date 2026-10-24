package com.gdproduct.rendana.ui.screens.add.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.gdproduct.rendana.domain.model.TransactionType
import com.gdproduct.rendana.data.local.entity.TransactionCategoryEntity
import com.gdproduct.rendana.data.local.entity.TransactionEntity
import com.gdproduct.rendana.data.repository.TransactionCategoryRepository
import com.gdproduct.rendana.data.repository.TransactionRepository

import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TransactionUiState(
    val merchantName: String = "",
    val name: String = "",
    val amount: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val categories: List<TransactionCategoryEntity> = emptyList(),
    val selectedCategoryId: Int? = null,
    val year: Int = LocalDate.now().year,
    val month: Int? = LocalDate.now().monthValue,
    val day: Int? = LocalDate.now().dayOfMonth,
    val note: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

class TransactionViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: TransactionCategoryRepository
) : ViewModel() {
	private val allowedTextRegex = Regex("""^[\p{L}\p{N} .,!?;:'"()\-&%+/]*$""")
    private val today = LocalDate.now()
    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeCategories(TransactionType.EXPENSE)
    }

    private fun observeCategories(type: TransactionType) {
        viewModelScope.launch {
            categoryRepository.getByType(type.name).collect { categories ->
                _uiState.update { state ->
                    state.copy(
                        categories = categories,
                        selectedCategoryId = state.selectedCategoryId
                            ?.takeIf { id -> categories.any { it.id == id } }
                    )
                }
            }
        }
    }

    fun onMerchantNameChange(value: String) {
		if (!allowedTextRegex.matches(value)) return
		
        _uiState.update {
            it.copy(
                merchantName = value,
                error = null,
                isSaved = false
            )
        }
    }

    fun onNameChange(value: String) {
        if (!allowedTextRegex.matches(value)) return
        
        _uiState.update {
            it.copy(
                name = value,
                error = null,
                isSaved = false
            )
        }
    }

    fun onAmountChange(value: String) {
        val digits = value.filter(Char::isDigit)
        val parsed = digits.toLongOrNull()

        if (digits.isNotEmpty() && parsed == null) return

        val formatted = if (digits.isEmpty()) {
            ""
        } else {
            NumberFormat.getIntegerInstance(Locale("id", "ID"))
                .format(parsed)
        }

        _uiState.update {
            it.copy(amount = formatted, error = null, isSaved = false)
        }
    }

    fun onTypeChange(type: TransactionType) {
        if (_uiState.value.type == type) return

        _uiState.update {
            it.copy(
                type = type,
                categories = emptyList(),
                selectedCategoryId = null,
                error = null,
                isSaved = false
            )
        }

        observeCategories(type)
    }

    fun onCategoryChange(categoryId: Int) {
        _uiState.update { state ->
            if (state.categories.none { it.id == categoryId }) {
                state
            } else {
                state.copy(
                    selectedCategoryId = categoryId,
                    error = null,
                    isSaved = false
                )
            }
        }
    }

    fun onYearChange(year: Int) {
        if (year !in 1900..today.year) return

        _uiState.update {
            it.copy(
                year = year,
                month = null,
                day = null,
                error = null,
                isSaved = false
            )
        }
    }

    fun onMonthChange(month: Int) {
        val state = _uiState.value
        if (month !in 1..12) return

        val latestMonth = if (state.year == today.year) {
            today.monthValue
        } else {
            12
        }

        if (month > latestMonth) return

        _uiState.update {
            it.copy(
                month = month,
                day = null,
                error = null,
                isSaved = false
            )
        }
    }

    fun onDayChange(day: Int) {
        val state = _uiState.value
        val month = state.month ?: return

        val yearMonth = runCatching {
            YearMonth.of(state.year, month)
        }.getOrNull() ?: return

        val maxDay = if (
            yearMonth.year == today.year &&
            yearMonth.monthValue == today.monthValue
        ) {
            minOf(yearMonth.lengthOfMonth(), today.dayOfMonth)
        } else {
            yearMonth.lengthOfMonth()
        }

        if (day !in 1..maxDay) return

        _uiState.update {
            it.copy(day = day, error = null, isSaved = false)
        }
    }

    fun onNoteChange(value: String) {
        if (!allowedTextRegex.matches(value)) return
        
        _uiState.update {
            it.copy(note = value, error = null, isSaved = false)
        }
    }

    fun availableMonths(): List<Int> {
        val state = _uiState.value
        if (state.year !in 1900..today.year) return emptyList()

        val maxMonth = if (state.year == today.year) {
            today.monthValue
        } else {
            12
        }

        return (1..maxMonth).toList()
    }

    fun availableDays(): List<Int> {
        val state = _uiState.value
        val month = state.month ?: return emptyList()

        val yearMonth = runCatching {
            YearMonth.of(state.year, month)
        }.getOrNull() ?: return emptyList()

        val maxDay = if (
            state.year == today.year &&
            month == today.monthValue
        ) {
            minOf(yearMonth.lengthOfMonth(), today.dayOfMonth)
        } else {
            yearMonth.lengthOfMonth()
        }

        return (1..maxDay).toList()
    }

    fun saveTransaction() {
        val state = _uiState.value

        if (state.isSaving) return

        val amount = state.amount
            .filter(Char::isDigit)
            .toLongOrNull()

        val categoryId = state.selectedCategoryId

        val selectedDate = if (
            state.month != null && state.day != null
        ) {
            runCatching {
                LocalDate.of(state.year, state.month, state.day)
            }.getOrNull()
        } else {
            null
        }

        val error = when {
            state.name.isBlank() ->
                "Nama transaksi wajib diisi."

            amount == null || amount <= 0L ->
                "Nominal harus lebih besar dari 0."

            categoryId == null ||
                state.categories.none { it.id == categoryId } ->
                "Pilih kategori transaksi."

            selectedDate == null ->
                "Pilih tanggal transaksi yang lengkap."

            selectedDate.isAfter(today) ->
                "Tanggal transaksi tidak boleh di masa depan."

            else -> null
        }

        if (error != null) {
            _uiState.update { it.copy(error = error) }
            return
        }

        val transaction = TransactionEntity(
            merchantName = state.merchantName.trim(),
            name = state.name.trim(),
            amount = amount!!,
            type = state.type.name,
            categoryId = categoryId!!,
            date = selectedDate!!.format(
                DateTimeFormatter.ISO_LOCAL_DATE
            ),
            note = state.note.trim().ifBlank { null }
        )

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSaving = true, error = null, isSaved = false)
            }

            try {
                transactionRepository.insertTransaction(transaction)

                _uiState.update {
                    it.copy(isSaving = false, isSaved = true)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Gagal menyimpan transaksi. Silakan coba lagi."
                    )
                }
            }
        }
    }

    fun resetForm() {
        _uiState.update {
            TransactionUiState(
                categories = it.categories,
                year = today.year,
                month = today.monthValue,
                day = today.dayOfMonth
            )
        }
    }
}