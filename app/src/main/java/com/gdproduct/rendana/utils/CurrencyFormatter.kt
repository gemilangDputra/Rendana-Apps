package com.gdproduct.rendana.utils
import java.util.Locale

enum class AmountFormat {
    FULL,
    COMPACT
}

fun formatAmount(
    amount: Long,
    format: AmountFormat = AmountFormat.FULL
): String {
    val absoluteAmount = kotlin.math.abs(amount)
    val formatted = when (format) {
        AmountFormat.FULL -> {
            "%,d".format(Locale.US, amount).replace(',', '.')
        }
		
        AmountFormat.COMPACT -> {
            when {
                absoluteAmount >= 1_000_000_000L ->
                    "%.1f M".format(
                        Locale.US,
                        amount / 1_000_000_000.0
                    )
				
                absoluteAmount >= 1_000_000L ->
                    "%.1f jt".format(
                        Locale.US,
                        amount / 1_000_000.0
                    )
				
                else ->
                    "%,d".format(Locale.US, amount).replace(',', '.')
            }
        }
    }
	
    return "Rp$formatted"
}