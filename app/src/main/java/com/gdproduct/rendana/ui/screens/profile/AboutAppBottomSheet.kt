package com.gdproduct.rendana.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import com.gdproduct.rendana.data.app.GetAppVersion
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppBottomSheet(
    onDismiss: () -> Unit
) {
	val context = LocalContext.current
	
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = RendanaThemeColors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tentang Aplikasi",
                style = MaterialTheme.typography.titleLarge,
                color = RendanaThemeColors.textPrimary
            )
			
            Text(
                text = "Rendana membantu Anda mencatat pemasukan dan pengeluaran, memantau saldo, serta mengelola keuangan pribadi dengan lebih mudah. Dengan pencatatan yang teratur, Anda dapat memahami kebiasaan finansial dan merencanakan penggunaan uang dengan lebih bijak.",
                style = MaterialTheme.typography.bodyMedium,
                color = RendanaThemeColors.textSecondary
            )
			
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Versi: ${context.GetAppVersion()}",
                style = MaterialTheme.typography.bodySmall,
                color = RendanaThemeColors.textTertiary
            )
        }
    }
}
