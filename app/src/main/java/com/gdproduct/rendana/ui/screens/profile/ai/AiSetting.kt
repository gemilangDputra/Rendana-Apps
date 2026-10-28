package com.gdproduct.rendana.ui.screens.profile.ai

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun AiSettings(
    onDismiss: () -> Unit,
    viewModel: AiSettingsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Pengaturan Kecerdasan Buatan",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            // Provider AI
            Text(
                text = "Pilih AI",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.selectProvider("Gemini")
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = uiState.selectedProvider == "Gemini",
                    onClick = {
                        viewModel.selectProvider("Gemini")
                    }
                )

                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Google Gemini")
                    Text(
                        text = "AI untuk membaca dan memahami struk",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider()

            // Model utama
            Text(
                text = "Model aktif",
                style = MaterialTheme.typography.titleMedium
            )

            viewModel.models.forEach { model ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.selectModel(model.id)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.selectedModel == model.id,
                        onClick = {
                            viewModel.selectModel(model.id)
                        }
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = model.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = model.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            HorizontalDivider()

            // Strategi kegagalan
            Text(
                text = "Jika model gagal",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Pilih satu tindakan yang akan dilakukan ketika pemrosesan struk gagal.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            FailureStrategyOption(
                title = "Coba ulang model yang sama",
                description = "Ulangi permintaan sebelum menyatakan gagal.",
                selected = uiState.failureStrategy ==
                    AiSettingsViewModel.STRATEGY_RETRY,
                onClick = {
                    viewModel.selectFailureStrategy(
                        AiSettingsViewModel.STRATEGY_RETRY
                    )
                }
            )

            FailureStrategyOption(
                title = "Gunakan model cadangan",
                description = "Alihkan pemrosesan ke model alternatif.",
                selected = uiState.failureStrategy ==
                    AiSettingsViewModel.STRATEGY_FALLBACK,
                onClick = {
                    viewModel.selectFailureStrategy(
                        AiSettingsViewModel.STRATEGY_FALLBACK
                    )
                }
            )

            FailureStrategyOption(
                title = "Langsung tampilkan feedback",
                description = "Hentikan pemrosesan dan tampilkan pesan error.",
                selected = uiState.failureStrategy ==
                    AiSettingsViewModel.STRATEGY_FEEDBACK,
                onClick = {
                    viewModel.selectFailureStrategy(
                        AiSettingsViewModel.STRATEGY_FEEDBACK
                    )
                }
            )

            // Pengaturan khusus retry
            if (
                uiState.failureStrategy ==
                AiSettingsViewModel.STRATEGY_RETRY
            ) {
                HorizontalDivider()

                Text(
                    text = "Jumlah percobaan ulang",
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "${uiState.retryCount} kali",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Slider(
                    value = uiState.retryCount.toFloat(),
                    onValueChange = {
                        viewModel.setRetryCount(it.toInt())
                    },
                    valueRange = 0f..5f,
                    steps = 4
                )

                Text(
                    text = "0 berarti tidak ada percobaan ulang. Maksimal 5 kali setelah permintaan awal.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Model cadangan
            if (
                uiState.failureStrategy ==
                AiSettingsViewModel.STRATEGY_FALLBACK
            ) {
                HorizontalDivider()

                Text(
                    text = "Model cadangan",
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "Model ini digunakan jika model utama gagal.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                viewModel.models
                    .filter { it.id != uiState.selectedModel }
                    .forEach { model ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectFallbackModel(model.id)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = uiState.selectedFallbackModel ==
                                    model.id,
                                onClick = {
                                    viewModel.selectFallbackModel(model.id)
                                }
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            ) {
                                Text(
                                    text = model.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = model.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
            }

            HorizontalDivider()

            // API key
            Text(
                text = "Kredensial Gemini",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "API key digunakan untuk mengakses layanan Gemini.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = uiState.apiKey,
                onValueChange = viewModel::onApiKeyChanged,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Gemini API Key")
                },
                placeholder = {
                    Text("Masukkan API key")
                },
                singleLine = true,
                visualTransformation = if (uiState.isKeyVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                
                trailingIcon = {
                    IconButton(
                        onClick = viewModel::toggleKeyVisibility
                    ) {
                        Icon(
                            imageVector = if (uiState.isKeyVisible) {
                                Icons.Outlined.Visibility
                            } else {
                                Icons.Outlined.VisibilityOff
                            },
                            contentDescription = if (uiState.isKeyVisible) {
                                "Sembunyikan API key"
                            } else {
                                "Tampilkan API key"
                            }
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            uiState.message?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (uiState.isSaved) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Batal")
                }

                Button(
                    onClick = viewModel::saveSettings,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Simpan")
                }
            }
        }
    }
}

@Composable
private fun FailureStrategyOption(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
