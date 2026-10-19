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

import com.gdproduct.rendana.domain.model.TransactionType
import com.gdproduct.rendana.ui.components.TransactionCard
import com.gdproduct.rendana.ui.icons.CalendarClock
import com.gdproduct.rendana.ui.icons.Person
import com.gdproduct.rendana.ui.icons.Notifications
import com.gdproduct.rendana.ui.icons.Receipt
import com.gdproduct.rendana.ui.icons.Savings
import com.gdproduct.rendana.ui.theme.RendanaThemeColors

@Composable
fun HomeScreen() {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
			BalanceSummaryCard(4968000L, 5000000L, 35000L)
			
            TransactionCard(
                title = "Makan siang",
                amount = 35000L,
                type = TransactionType.EXPENSE,
                category = "Makanan & Minuman",
                date = "14 Okt 2026",
                icon = Icons.Filled.Restaurant,
                note = "Makan bersama teman"
            )

            TransactionCard(
                title = "Gaji bulanan",
                amount = 5000000L,
                type = TransactionType.INCOME,
                category = "Gaji",
                date = "14 Okt 2026",
                icon = Icons.Filled.Wallet
            )
        }
    }
}