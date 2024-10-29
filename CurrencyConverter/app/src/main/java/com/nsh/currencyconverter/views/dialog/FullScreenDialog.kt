package com.nsh.currencyconverter.views.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.nsh.currencyconverter.R
import com.nsh.currencyconverter.utils.formatCurrency

class FullScreenDialog(
    context: Context,
    private val message: String,
    private val dataPoints: List<Entry>
) : Dialog(context) {

    private lateinit var lineChart: LineChart
    private lateinit var dialogMessage: TextView
    private lateinit var closeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_fullscreen)

        dialogMessage = findViewById(R.id.titleDialog)
        closeButton = findViewById(R.id.closeButton)
        lineChart = findViewById(R.id.lineChart)

        dialogMessage.text = message

        setupChart()

        closeButton.setOnClickListener { dismiss() }
    }

    private fun setupChart() {

        val formattedDataPoints = dataPoints.map { entry ->
            Entry(entry.x, entry.y, formatCurrency(entry.y.toDouble()))
        }

        val lineDataSet = LineDataSet(formattedDataPoints, "Exchange Rates").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            valueTextSize = 14f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return formatCurrency(value.toDouble())
                }
            }
        }

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData
        lineChart.invalidate()
    }

}
