package com.gdproduct.rendana.ui.screens.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Wallet

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.gdproduct.rendana.domain.model.TransactionType
import com.gdproduct.rendana.ui.components.TransactionCard
import com.gdproduct.rendana.ui.icons.getIcon
import com.gdproduct.rendana.ui.icons.CalendarClock
import com.gdproduct.rendana.ui.icons.Person
import com.gdproduct.rendana.ui.icons.Notifications
import com.gdproduct.rendana.ui.icons.Receipt
import com.gdproduct.rendana.ui.icons.Savings
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel()
) {
	val transactions by homeViewModel.transactions.collectAsState()
	
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
		
        
		Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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