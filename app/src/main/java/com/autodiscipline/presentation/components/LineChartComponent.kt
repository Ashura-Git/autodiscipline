package com.autodiscipline.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import android.graphics.Color

@Composable
fun LineChartComponent(modifier: Modifier = Modifier, entries: List<Entry>, labels: List<String>) {
    AndroidView(
        modifier = modifier,
        factory = {
            LineChart(it).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
                setDrawGridBackground(false)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                    textColor = Color.BLACK
                    valueFormatter = IndexAxisValueFormatter(labels)
                    granularity = 1f
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                    textColor = Color.BLACK
                }

                axisRight.isEnabled = false

                legend.isEnabled = false
            }
        },
        update = {
            val dataSet = LineDataSet(entries, "Label").apply {
                color = Color.BLUE
                setCircleColor(Color.BLUE)
                lineWidth = 2f
                circleRadius = 4f
                setDrawCircleHole(false)
                valueTextSize = 0f
            }
            it.data = LineData(dataSet)
            it.invalidate()
        }
    )
}
