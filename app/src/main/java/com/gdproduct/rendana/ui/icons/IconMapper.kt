package com.gdproduct.rendana.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector

fun getIcon(name: String): ImageVector {
    return when (name.lowercase()) {
        "accountbalance" -> AccountBalance
        "add" -> Add
        "calendarclock" -> CalendarClock
        "category" -> Category
        "home" -> Home
        "menu" -> Menu
        "monitoring" -> Monitoring
        "notifications" -> Notifications
        "paid" -> Paid
        "payments" -> Payments
        "person" -> Person
        "receipt" -> Receipt
        "receiptlong" -> ReceiptLong
        "redeem" -> Redeem
        "restaurant" -> Restaurant
        "savings" -> Savings
        "shoppingbag" -> ShoppingBag
        "store" -> Store
        "transportation" -> Transportation
        "trendingdown" -> TrendingDown
        "trendingup" -> TrendingUp
        "wallet" -> Wallet
		"healthcross" -> HealthCross
        "vaccines" -> Vaccines
        "school" -> School
        "wifi" -> Wifi
        "electricbolt" -> ElectricBolt
        else -> Category
    }
}
