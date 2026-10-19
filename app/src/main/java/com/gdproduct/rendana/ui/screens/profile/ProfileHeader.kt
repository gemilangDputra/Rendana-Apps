package com.gdproduct.rendana.ui.screens.profile

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material3.Icon
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext

import coil.compose.AsyncImage
import coil.request.ImageRequest

import com.gdproduct.rendana.ui.icons.Notifications
import com.gdproduct.rendana.ui.icons.Person
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

@Composable
fun ProfileHeader(
    nickname: String,
    profilePicture: String
) {
	val context = LocalContext.current
    val imageRequest = remember(profilePicture, context) {
        if (
            profilePicture.isNotBlank() &&
            profilePicture != "Defaults"
        ) {
            ImageRequest.Builder(context)
                .data(Uri.parse(profilePicture))
                .memoryCacheKey(profilePicture)
                .build()
        } else {
            null
        }
    }
	
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(RendanaThemeColors.primary)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Notifications,
                contentDescription = "Notifikasi",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(27.dp),
                tint = RendanaThemeColors.iconOnPrimary
            )
			
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (imageRequest != null) {
                    AsyncImage(
                        model = imageRequest,
                        contentDescription = "Foto profil",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                color = RendanaThemeColors.primaryDark,
                                shape = CircleShape
                            ),
                            contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Person,
                            contentDescription = "Foto profil default",
                            modifier = Modifier.size(42.dp),
                            tint = RendanaThemeColors.iconOnPrimary
                        )
                    }
                }
				
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = nickname,
                    color = RendanaThemeColors.textOnPrimary
                )
            }
        }
    }
}
