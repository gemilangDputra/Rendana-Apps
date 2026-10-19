package com.gdproduct.rendana.ui.screens.profile.settings

import android.content.Intent
import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel

import coil.compose.AsyncImage

import com.gdproduct.rendana.ui.icons.Person
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettings(
    onDismiss: () -> Unit,
    viewModel: ProfileSettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    val savedNickname by viewModel.nickname.collectAsState()
    val savedProfilePicture by viewModel.profilePicture.collectAsState()
    var nicknameInput by rememberSaveable { mutableStateOf("User") }
    var selectedProfilePicture by rememberSaveable { mutableStateOf("Defaults") }
	
    LaunchedEffect(savedNickname) {
        nicknameInput = savedNickname
    }
	
    LaunchedEffect(savedProfilePicture) {
        selectedProfilePicture = savedProfilePicture
    }
	
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
            } catch (_: UnsupportedOperationException) {
            }
            
            selectedProfilePicture = uri.toString()
        }
    }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = RendanaThemeColors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Pengaturan Profil",
                style = MaterialTheme.typography.titleLarge,
                color = RendanaThemeColors.textPrimary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            if (
                selectedProfilePicture.isNotBlank() &&
                selectedProfilePicture != "Defaults"
            ) {
                AsyncImage(
                    model = selectedProfilePicture,
                    contentDescription = "Foto profil",
                    modifier = Modifier
                        .size(104.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    onError = {}
                )
            } else {
                Icon(
                    imageVector = Person,
                    contentDescription = "Foto profil default",
                    modifier = Modifier.size(104.dp),
                    tint = RendanaThemeColors.primaryLight
                )
            }
            
            Button(
                onClick = {
                    photoPicker.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts
                                .PickVisualMedia.ImageOnly
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ubah Foto Profil")
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = nicknameInput,
                onValueChange = { nicknameInput = it },
                label = { Text("Nickname") },
                placeholder = { Text("Masukkan nickname") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            
            Button(
                onClick = {
                    val newNickname = nicknameInput.trim()
                    if (newNickname.isNotBlank()) {
                        viewModel.saveProfileChanges(
                            nickname = newNickname,
                            profilePicture = selectedProfilePicture
                        )
                        onDismiss()
                    }
                },
                enabled = nicknameInput.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ubah")
            }
        }
    }
}