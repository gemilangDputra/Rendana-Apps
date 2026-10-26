package com.gdproduct.rendana.ui.screens.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.core.cartesian.marker.LineCartesianLayerMarkerTarget

import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ReportChartCard(
    data: List<ReportChartData>,
    chartIncome: Color,
    chartExpense: Color,
    chartGrid: Color,
    chartAxis: Color,
    chartTooltipBackground: Color,
    chartTooltipText: Color,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember(data) {
        mutableIntStateOf(-1)
    }

    val modelProducer = remember {
        CartesianChartModelProducer()
    }

	LaunchedEffect(data, modelProducer) {
        if (data.isEmpty()) {
            return@LaunchedEffect
        }
        
        modelProducer.runTransaction {
            lineSeries {
                series(data.map { it.income.toDouble() })
                series(data.map { it.expense.toDouble() })
            }
        }
    }


    val incomeLine = LineCartesianLayer.rememberLine(
        fill = LineCartesianLayer.LineFill.single(fill(chartIncome))
    )

    val expenseLine = LineCartesianLayer.rememberLine(
        fill = LineCartesianLayer.LineFill.single(fill(chartExpense))
    )

    val tooltipTextComponent = rememberTextComponent(
        color = chartTooltipText,
        textSize = 12.sp
    )

    // DIPERBAIKI: Langsung panggil rememberDefaultCartesianMarker tanpa dibungkus remember {} tambahan
    val marker = rememberDefaultCartesianMarker(
        label = tooltipTextComponent
    )

    val axisLabelStart = rememberTextComponent(
        color = chartAxis,
        textSize = 10.sp
    )

    val axisLabelBottom = rememberTextComponent(
        color = chartAxis,
        textSize = 10.sp
    )

    val chart = rememberCartesianChart(
        rememberLineCartesianLayer(
            lineProvider = LineCartesianLayer.LineProvider.series(
                incomeLine,
                expenseLine
            )
        ),
        startAxis = VerticalAxis.rememberStart(
            label = axisLabelStart
        ),
        bottomAxis = HorizontalAxis.rememberBottom(
            label = axisLabelBottom
        ),
        markerVisibilityListener = remember(data) {
            object : CartesianMarkerVisibilityListener {
                override fun onShown(
                    marker: CartesianMarker,
                    targets: List<CartesianMarker.Target>
                ) {
                    updateSelectedIndex(targets, data.size) {
                        selectedIndex = it
                    }
                }

                override fun onUpdated(
                    marker: CartesianMarker,
                    targets: List<CartesianMarker.Target>
                ) {
                    updateSelectedIndex(targets, data.size) {
                        selectedIndex = it
                    }
                }

                override fun onHidden(marker: CartesianMarker) {
                    selectedIndex = -1
                }
            }
        },
        marker = marker
    )

	val selectedData = data.getOrNull(selectedIndex)
	
    val cumulativeTotals = remember(data) {
        val totals = ArrayList<Pair<Double, Double>>(data.size)
        var incomeTotal = 0.0
        var expenseTotal = 0.0

        data.forEach { item ->
            incomeTotal += item.income
            expenseTotal += item.expense
            totals.add(incomeTotal to expenseTotal)
        }

        totals
    }
    
    val selectedTotals = cumulativeTotals.getOrNull(selectedIndex)
    
    val totalIncome = selectedTotals?.first
        ?: cumulativeTotals.lastOrNull()?.first
        ?: 0.0

    val totalExpense = selectedTotals?.second
        ?: cumulativeTotals.lastOrNull()?.second
        ?: 0.0

    val rupiahFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
            maximumFractionDigits = 0
        }
    }

    val dateFormat = remember {
        DateTimeFormatter.ofPattern("d MMM", Locale("id", "ID"))
    }
	
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pendapatan",
                        color = chartIncome,
                        style = MaterialTheme.typography.labelMedium
                    )
					Text(
                        text = rupiahFormat.format(totalIncome),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pengeluaran",
                        color = chartExpense,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = rupiahFormat.format(totalExpense),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (selectedData != null) {
                        "Akumulasi hingga ${selectedData.date.format(dateFormat)}"
                    } else {
                        "Akumulasi seluruh periode"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = chartAxis
                )

                if (selectedIndex in data.indices) {
                    Text(
                        text = data[selectedIndex].date.format(dateFormat),
                        style = MaterialTheme.typography.bodySmall,
                        color = chartAxis
                    )
                }
            }

            if (data.isNotEmpty()) {
                CartesianChartHost(
                    chart = chart,
                    modelProducer = modelProducer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            } else {
                Text(
                    text = "Belum ada data untuk ditampilkan",
                    color = chartAxis
                )
            }
        }
    }
}

private fun updateSelectedIndex(
    targets: List<CartesianMarker.Target>,
    dataSize: Int,
    onSelected: (Int) -> Unit
) {
    if (dataSize == 0) return

    val x = targets
        .filterIsInstance<LineCartesianLayerMarkerTarget>()
        .firstOrNull()
        ?.points
        ?.firstOrNull()
        ?.entry
        ?.x
        ?: return

    onSelected(x.toInt().coerceIn(0, dataSize - 1))
}