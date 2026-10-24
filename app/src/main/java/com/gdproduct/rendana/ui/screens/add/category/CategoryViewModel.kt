package com.gdproduct.rendana.ui.screens.add.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.gdproduct.rendana.data.local.RendanaDatabase
import com.gdproduct.rendana.data.local.entity.TransactionCategoryEntity
import com.gdproduct.rendana.data.repository.TransactionCategoryRepository
import com.gdproduct.rendana.domain.model.TransactionType

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CategoryUiState(
    val name: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val iconName: String = "category",
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val errorMessage: String? = null
)

class CategoryViewModel(application: Application) :
    AndroidViewModel(application) {

    private val database = RendanaDatabase.getDatabase(application)

    private val repository = TransactionCategoryRepository(
        database.transactionCategoryDao()
    )

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.value = _uiState.value.copy(
            name = value,
            saved = false,
            errorMessage = null
        )
    }

    fun onTypeChange(value: TransactionType) {
        _uiState.value = _uiState.value.copy(
            type = value,
            saved = false,
            errorMessage = null
        )
    }

    fun onIconChange(value: String) {
        _uiState.value = _uiState.value.copy(
            iconName = value,
            saved = false
        )
    }

    fun saveCategory() {
        val current = _uiState.value
        val categoryName = current.name.trim()

        if (categoryName.isBlank()) {
            _uiState.value = current.copy(
                errorMessage = "Nama kategori tidak boleh kosong."
            )
            return
        }

        if (current.isSaving) return

        viewModelScope.launch {
            _uiState.value = current.copy(
                isSaving = true,
                saved = false,
                errorMessage = null
            )

            try {
                repository.insertCategory(
                    TransactionCategoryEntity(
                        type = current.type.name,
                        name = categoryName,
                        iconName = current.iconName
                    )
                )

                _uiState.value = CategoryUiState(saved = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "Kategori gagal disimpan. Coba lagi."
                )
            }
        }
    }
}