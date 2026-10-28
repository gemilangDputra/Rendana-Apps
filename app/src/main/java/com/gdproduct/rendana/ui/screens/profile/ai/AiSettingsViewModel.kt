package com.gdproduct.rendana.ui.screens.profile.ai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gdproduct.rendana.data.local.security.SecureStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AiModelOption(
    val id: String,
    val name: String,
    val description: String
)

data class AiSettingsUiState(
    val selectedProvider: String = "Gemini",
    val selectedModel: String = "gemini-3.6-flash",
    val selectedFallbackModel: String = "gemini-3.8-flash",
    val failureStrategy: String = "fallback",
    val retryCount: Int = 2,
    val apiKey: String = "",
    val isKeyVisible: Boolean = false,
    val message: String? = null,
    val isSaved: Boolean = false
)

class AiSettingsViewModel(
    application: Application
) : AndroidViewModel(application) {

    companion object {
        const val STRATEGY_RETRY = "retry"
        const val STRATEGY_FALLBACK = "fallback"
        const val STRATEGY_FEEDBACK = "feedback"
    }

    private val secureStorage = SecureStorage(
        application.applicationContext
    )

    private val preferences = application.getSharedPreferences(
        "ai_settings",
        Application.MODE_PRIVATE
    )

    val models = listOf(
        AiModelOption(
            id = "gemini-3.8-flash",
            name = "3.8 Flash",
            description = "Model Flash"
        ),
        AiModelOption(
            id = "gemini-3.6-flash",
            name = "3.6 Flash",
            description = "Model Flash"
        ),
        AiModelOption(
            id = "gemini-3.5-flash-lite",
            name = "3.5 Flash Lite",
            description = "Model ringan"
        )
    )

    private val defaultModel = "gemini-3.6-flash"
    private val defaultFallbackModel = "gemini-3.8-flash"

    private val _uiState = MutableStateFlow(
        AiSettingsUiState(
            selectedProvider = preferences.getString(
                "selected_provider",
                "Gemini"
            ) ?: "Gemini",

            selectedModel = preferences.getString(
                "selected_model",
                defaultModel
            ) ?: defaultModel,

            selectedFallbackModel = preferences.getString(
                "selected_fallback_model",
                defaultFallbackModel
            ) ?: defaultFallbackModel,

            failureStrategy = preferences.getString(
                "failure_strategy",
                STRATEGY_FALLBACK
            ) ?: STRATEGY_FALLBACK,

            retryCount = preferences.getInt(
                "retry_count",
                2
            ).coerceIn(0, 5)
        )
    )

    val uiState: StateFlow<AiSettingsUiState> =
        _uiState.asStateFlow()

    init {
        ensureDifferentModels()
        loadApiKey()
    }

    private fun ensureDifferentModels() {
        val state = _uiState.value

        if (state.selectedFallbackModel == state.selectedModel) {
            val alternative = models.firstOrNull {
                it.id != state.selectedModel
            }?.id ?: return

            _uiState.value = state.copy(
                selectedFallbackModel = alternative
            )
        }
    }

    private fun loadApiKey() {
        try {
            _uiState.value = _uiState.value.copy(
                apiKey = secureStorage.getApiKey().orEmpty()
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(
                message = "Gagal membaca API key.",
                isSaved = false
            )
        }
    }

    fun selectProvider(provider: String) {
        _uiState.value = _uiState.value.copy(
            selectedProvider = provider,
            message = null,
            isSaved = false
        )
    }

    fun selectModel(modelId: String) {
        val state = _uiState.value

        val newFallback = if (
            state.selectedFallbackModel == modelId
        ) {
            models.firstOrNull { it.id != modelId }?.id
                ?: state.selectedFallbackModel
        } else {
            state.selectedFallbackModel
        }

        _uiState.value = state.copy(
            selectedModel = modelId,
            selectedFallbackModel = newFallback,
            message = null,
            isSaved = false
        )
    }

    fun selectFallbackModel(modelId: String) {
        val state = _uiState.value

        if (modelId == state.selectedModel) return

        _uiState.value = state.copy(
            selectedFallbackModel = modelId,
            message = null,
            isSaved = false
        )
    }

    fun selectFailureStrategy(strategy: String) {
        if (
            strategy !in listOf(
                STRATEGY_RETRY,
                STRATEGY_FALLBACK,
                STRATEGY_FEEDBACK
            )
        ) return

        _uiState.value = _uiState.value.copy(
            failureStrategy = strategy,
            message = null,
            isSaved = false
        )
    }

    fun setRetryCount(count: Int) {
        _uiState.value = _uiState.value.copy(
            retryCount = count.coerceIn(0, 5),
            message = null,
            isSaved = false
        )
    }

    fun onApiKeyChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            apiKey = value,
            message = null,
            isSaved = false
        )
    }

    fun toggleKeyVisibility() {
        _uiState.value = _uiState.value.copy(
            isKeyVisible = !_uiState.value.isKeyVisible
        )
    }

    fun saveSettings() {
        val state = _uiState.value

        try {
            val key = state.apiKey.trim()

            if (key.isBlank()) {
                secureStorage.clearApiKey()
            } else {
                secureStorage.saveApiKey(key)
            }

            preferences.edit()
                .putString("selected_provider", state.selectedProvider)
                .putString("selected_model", state.selectedModel)
                .putString(
                    "selected_fallback_model",
                    state.selectedFallbackModel
                )
                .putString("failure_strategy", state.failureStrategy)
                .putInt("retry_count", state.retryCount)
                .apply()

            _uiState.value = state.copy(
                apiKey = key,
                message = "Pengaturan AI berhasil disimpan.",
                isSaved = true
            )
        } catch (_: Exception) {
            _uiState.value = state.copy(
                message = "Gagal menyimpan pengaturan AI.",
                isSaved = false
            )
        }
    }
}