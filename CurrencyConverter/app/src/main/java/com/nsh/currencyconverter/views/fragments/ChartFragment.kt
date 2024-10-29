package com.nsh.currencyconverter.views.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nsh.currencyconverter.R
import java.text.SimpleDateFormat
import java.util.*

class ChartFragment : Fragment() {

    private lateinit var fromDateTextView: TextView
    private lateinit var toDateTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chart, container, false)

        fromDateTextView = view.findViewById(R.id.fromDate)
        toDateTextView = view.findViewById(R.id.toDate)

        fromDateTextView.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                fromDateTextView.text = selectedDate
            }
        }

        toDateTextView.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                toDateTextView.text = selectedDate
            }
        }

        return view
    }

    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Định dạng ngày theo kiểu yyyy-MM-dd
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                onDateSelected(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}
