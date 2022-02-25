package com.simplifyd.zerodata.android.utils

import android.graphics.Color
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.simplifyd.zerodata.android.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


fun PieChart.draw(entries: List<PieEntry>, title: String) {
    val dataSet = PieDataSet(entries, title).apply {
        colors = allColors
        valueTextSize = 16f
        valueTypeface = ResourcesCompat.getFont(context, R.font.gt_walsheim_medium_web)
        sliceSpace = 1f
        valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.roundToInt()} %"
            }
        }

        form = Legend.LegendForm.CIRCLE
    }

    legend.apply {
        verticalAlignment = Legend.LegendVerticalAlignment.TOP
        horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        orientation = Legend.LegendOrientation.VERTICAL
        setDrawInside(false)
    }

    data = PieData(dataSet)
    data.setValueTypeface(ResourcesCompat.getFont(context, R.font.gt_walsheim_medium_web))
    setEntryLabelTypeface(ResourcesCompat.getFont(context, R.font.gt_walsheim_medium_web))
    setNoDataTextTypeface(ResourcesCompat.getFont(context, R.font.gt_walsheim_medium_web))
    setCenterTextTypeface(ResourcesCompat.getFont(context, R.font.gt_walsheim_medium_web))

    val typeFace = ResourcesCompat.getFont(
        context,
        R.font.gt_walsheim_medium_web
    )
    // entry label styling
    setEntryLabelColor(Color.WHITE)
    setEntryLabelTypeface(
        typeFace
    )
    setDrawEntryLabels(false)
    setEntryLabelTextSize(12f)

    //chart.transparentCircleRadius = 61f
    isDrawHoleEnabled = true
    setUsePercentValues(true)

    val chartLegend = legend
    chartLegend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
    chartLegend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
    chartLegend.orientation = Legend.LegendOrientation.VERTICAL
    chartLegend.setDrawInside(false)
    chartLegend.typeface = typeFace
    chartLegend.xEntrySpace = 7f
    chartLegend.yEntrySpace = 3f
    chartLegend.yOffset = 0f

    setUsePercentValues(true)
    holeRadius = 70f
    description.isEnabled = false
    setExtraOffsets(0f, 0f, 10f, -55f)
    transparentCircleRadius = 15f
    rotationAngle = 0f

    animateY(1000, Easing.EaseInOutQuad)
    setDrawEntryLabels(false)
    invalidate()
}


fun BarChart.draw(entries: List<BarEntry>, title: String, labels: List<String>) {
    val typeFace = ResourcesCompat.getFont(context, R.font.gt_walsheim_medium_web)
    val dataSet = BarDataSet(entries, title).apply {
        colors = allColors
        valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return ""
            }
        }
        valueTypeface = typeFace
    }

    data = BarData(dataSet)
    data.setValueTypeface(typeFace)
    setNoDataTextTypeface(typeFace)

    description.isEnabled = false

    val legendTypeFace = ResourcesCompat.getFont(
        context,
        R.font.gt_walsheim_medium_web
    )

    legend.apply {
        form = Legend.LegendForm.NONE
        horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        direction = Legend.LegendDirection.LEFT_TO_RIGHT
        typeface = legendTypeFace
        setDrawInside(false)
    }

    setExtraOffsets(0f, 0f, 0f, 10f)

    xAxis.apply {
        valueFormatter = IndexAxisValueFormatter(labels)
        setCenterAxisLabels(true)
        setDrawGridLines(false)
        setDrawAxisLine(false)
        granularity = 1f
        typeface = typeFace
        labelCount = labels.size
        labelRotationAngle = 270f
        setFitBars(true)
        position = XAxis.XAxisPosition.TOP_INSIDE
    }

    axisLeft.apply {
        setCenterAxisLabels(true)
        setDrawGridLines(false)
        setDrawAxisLine(false)
        typeface = typeFace
        axisMinimum = 0f
    }

    axisLeft.axisMinimum = 0f
    axisRight.isEnabled = false

    animateY(1000, Easing.EaseInOutQuad)
}

fun LineChart.draw(entries: List<List<Entry>>, titles: List<String>) {
    val chartData = LineData()
    val typeFace = ResourcesCompat.getFont(context, R.font.gt_walsheim_medium_web)

    entries.mapIndexed { index, entry ->
        chartData.addDataSet(LineDataSet(entry, titles[index]).apply {
            fillAlpha = 110
            color = allColors[index]
            lineWidth = 2f
            setDrawCircleHole(false)
            setCircleColor(allColors[index])
            valueTypeface = ResourcesCompat.getFont(context, R.font.gt_walsheim_medium_web)
            circleRadius = 1f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return ""
                }
            }
        })
    }

    data = chartData
    data.setValueTypeface(typeFace)
    setNoDataTextTypeface(typeFace)

    description.isEnabled = false
    axisRight.isEnabled = false

    setExtraOffsets(0f, 0f, 0f, 10f)
    axisLeft.isEnabled = true

    xAxis.apply {
        position = XAxis.XAxisPosition.TOP_INSIDE
        labelRotationAngle = 270f
        setCenterAxisLabels(true)
        setDrawGridLines(false)
        setDrawAxisLine(false)
        typeface = typeFace

        valueFormatter = object : ValueFormatter() {
            private val mFormat: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            override fun getFormattedValue(value: Float): String {
                return mFormat.format(Date(System.currentTimeMillis() + value.toLong()))
            }
        }
    }

    axisLeft.apply {
        setCenterAxisLabels(true)
        setDrawGridLines(false)
        setDrawAxisLine(false)
        typeface = typeFace
        axisMinimum = 0f
    }

    val l: Legend = legend
    l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
    l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
    l.typeface = typeFace
    l.orientation = Legend.LegendOrientation.HORIZONTAL
    l.setDrawInside(false)

    animateY(1000, Easing.EaseInOutQuad)
}