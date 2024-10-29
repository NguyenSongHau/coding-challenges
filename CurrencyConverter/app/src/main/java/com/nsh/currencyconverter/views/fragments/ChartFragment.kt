package com.nsh.currencyconverter.views.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.nsh.currencyconverter.R
import com.nsh.currencyconverter.adapters.CurrencyAdapter
import com.nsh.currencyconverter.controllers.ExchangeController
import com.nsh.currencyconverter.models.CurrencyDetails
import com.nsh.currencyconverter.utils.formatCurrency
import com.nsh.currencyconverter.utils.formatDate
import com.nsh.currencyconverter.views.dialog.FullScreenDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ChartFragment : Fragment() {
    private lateinit var convertFromDropDown: TextView
    private lateinit var convertToDropDown: TextView
    private lateinit var amount: EditText
    private lateinit var fromDateTextView: TextView
    private lateinit var toDateTextView: TextView
    private lateinit var exchangeButton: Button
    private lateinit var resultTextView: TextView
    private val exchangeController = ExchangeController()
    private var filteredCurrencyDetails = listOf<CurrencyDetails>()

    private var fromCurrency: String = ""
    private var toCurrency: String = ""
    private var amountValue: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chart, container, false)

        // Initialize UI components
        convertFromDropDown = view.findViewById(R.id.convertFromDropDown)
        convertToDropDown = view.findViewById(R.id.convertToDropDown)
        amount = view.findViewById(R.id.amount)
        fromDateTextView = view.findViewById(R.id.fromDate)
        toDateTextView = view.findViewById(R.id.toDate)
        exchangeButton = view.findViewById(R.id.exchangeButton)
        resultTextView = view.findViewById(R.id.resultTextView)

        // Set up listeners
        setUpListeners()

        return view
    }

    private fun setUpListeners() {
        convertFromDropDown.setOnClickListener {
            showCurrencyDialog { selectedCurrency ->
                fromCurrency = selectedCurrency
                convertFromDropDown.text = selectedCurrency
            }
        }

        convertToDropDown.setOnClickListener {
            showCurrencyDialog { selectedCurrency ->
                toCurrency = selectedCurrency
                convertToDropDown.text = selectedCurrency
            }
        }

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

        exchangeButton.setOnClickListener {
            handleExchangeButtonClick()
        }
    }

    private fun handleExchangeButtonClick() {
        val amountString = amount.text.toString()
        amountValue = amountString.toDoubleOrNull()

        val fromDateString = fromDateTextView.text.toString()
        val toDateString = toDateTextView.text.toString()

        if (fromCurrency.isEmpty() || toCurrency.isEmpty() || amountValue == null) {
            showAlertDialog("Error", "Please enter a valid amount and select currencies!")
            return
        }

        if (fromDateString.isEmpty() || toDateString.isEmpty()) {
            showAlertDialog("Error", "Please select both From date and To date!")
            return
        }

        // Fetch historical exchange rate
        fetchHistoricalExchangeRate(fromCurrency, toCurrency, fromDateString) { historicalRate ->
            // Fetch latest exchange rate
            exchangeController.fetchLatestExchangeRate(baseCurrency = fromCurrency, currencies = toCurrency) { latestRates ->
                val dataPoints = mutableListOf<Entry>()

                // Calculate historical converted value
                var historicalConvertedValue: Double? = null
                historicalRate?.let {
                    historicalConvertedValue = it * amountValue!! // Multiply historical rate by amount
                    dataPoints.add(Entry(0f, historicalConvertedValue!!.toFloat())) // Index 0 for historical rate
                }

                // Calculate latest converted value
                var latestConvertedValue: Double? = null
                latestRates?.let {
                    val latestRate = it[toCurrency]
                    latestRate?.let { rate ->
                        latestConvertedValue = rate * amountValue!! // Multiply latest rate by amount
                        dataPoints.add(Entry(1f, latestConvertedValue!!.toFloat())) // Index 1 for latest rate
                    }
                }

                // Format values for display
                val formattedHistoricalValue = historicalConvertedValue?.let { formatCurrency(it) } ?: "N/A"
                val formattedLatestValue = latestConvertedValue?.let { formatCurrency(it) } ?: "N/A"

                // Show message and calculated values
                val message = "Exchange rates from $fromCurrency to $toCurrency\n" +
                        "Historical: $formattedHistoricalValue\n" +
                        "Latest: $formattedLatestValue"
                val dialog = FullScreenDialog(requireContext(), message, dataPoints)
                dialog.show()
            }
        }
    }

    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = calendar.timeInMillis

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }.time

                val formattedDate = formatDate(selectedDate)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = yesterday
        datePickerDialog.datePicker.maxDate = today

        datePickerDialog.show()
    }

    private fun showCurrencyDialog(onCurrencySelected: (String) -> Unit) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.spinner)

        val lvCurrency = dialog.findViewById<ListView>(R.id.lvCurrency)
        val searchEditText = dialog.findViewById<EditText>(R.id.edtCurrency)

        exchangeController.fetchSymbols { symbols ->
            symbols?.let {
                val currencyDetailsList = it.map { entry -> entry.value }
                val currencyCodes = it.keys.toList()

                filteredCurrencyDetails = currencyDetailsList.toList()
                val adapter = CurrencyAdapter(requireContext(), filteredCurrencyDetails)
                lvCurrency.adapter = adapter

                searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val query = s.toString().lowercase()
                        filteredCurrencyDetails = currencyDetailsList.filter { it.name.lowercase().contains(query) }

                        adapter.clear()
                        adapter.addAll(filteredCurrencyDetails)
                        adapter.notifyDataSetChanged()
                    }
                })

                lvCurrency.setOnItemClickListener { _, _, position, _ ->
                    val selectedCurrencyCode = currencyCodes[currencyDetailsList.indexOf(filteredCurrencyDetails[position])]
                    onCurrencySelected(selectedCurrencyCode)
                    dialog.dismiss()
                }

                dialog.show()
            }
        }
    }

    private fun fetchHistoricalExchangeRate(baseCurrency: String, currencies: String, dateTo: String, onResult: (Double?) -> Unit) {
        exchangeController.fetchHistoricalExchangeRate(baseCurrency, currencies, dateTo) { exchangeRate ->
            onResult(exchangeRate)
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun parseDate(dateString: String, pattern: String = "yyyy-MM-dd"): Date? {
        return try {
            val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }
}