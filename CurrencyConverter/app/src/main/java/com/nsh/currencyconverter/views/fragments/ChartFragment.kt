package com.nsh.currencyconverter.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.nsh.currencyconverter.R
import com.nsh.currencyconverter.controllers.ExchangeController
import com.nsh.currencyconverter.models.CurrencyDetails
import com.nsh.currencyconverter.utils.hideKeyboard
import com.nsh.currencyconverter.utils.showAlertDialog
import com.nsh.currencyconverter.utils.showCurrencyDialog
import com.nsh.currencyconverter.utils.showDatePickerDialog
import com.nsh.currencyconverter.utils.showKeyboard
import com.nsh.currencyconverter.views.dialog.ChartDialog

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

        convertFromDropDown = view.findViewById(R.id.convertFromDropDown)
        convertToDropDown = view.findViewById(R.id.convertToDropDown)
        amount = view.findViewById(R.id.amount)
        fromDateTextView = view.findViewById(R.id.fromDate)
        toDateTextView = view.findViewById(R.id.toDate)
        exchangeButton = view.findViewById(R.id.exchangeButton)

        setUpListeners()

        amount.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showKeyboard(requireContext(), amount)
            }
        }

        view.setOnTouchListener { _, _ ->
            hideKeyboard(requireContext(), view)
            false
        }

        return view
    }

    private fun setUpListeners() {
        convertFromDropDown.setOnClickListener {
            showCurrencySelectionDialog { selectedCurrency ->
                fromCurrency = selectedCurrency
                convertFromDropDown.text = selectedCurrency
            }
        }

        convertToDropDown.setOnClickListener {
            showCurrencySelectionDialog { selectedCurrency ->
                toCurrency = selectedCurrency
                convertToDropDown.text = selectedCurrency
            }
        }

        fromDateTextView.setOnClickListener {
            showDatePickerDialog(requireContext()) { selectedDate ->
                fromDateTextView.text = selectedDate
            }
        }

        toDateTextView.setOnClickListener {
            showDatePickerDialog(requireContext()) { selectedDate ->
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
            showAlertDialog(requireContext(), "Error", "Please enter a valid amount and select currencies!")
            return
        }

        if (fromDateString.isEmpty() || toDateString.isEmpty()) {
            showAlertDialog(requireContext(), "Error", "Please select both From date and To date!")
            return
        }

        if (fromDateString > toDateString) {
            showAlertDialog(requireContext(), "Error", "From date cannot be later than To date!")
            return
        }

        fetchHistoricalExchangeRate(fromCurrency, toCurrency, fromDateString) { historicalRate ->
            exchangeController.fetchLatestExchangeRate(baseCurrency = fromCurrency, currencies = toCurrency) { latestRates ->
                val dataPoints = mutableListOf<Entry>()

                var historicalConvertedValue: Double? = null
                historicalRate?.let {
                    historicalConvertedValue = it * amountValue!!
                    dataPoints.add(Entry(0f, historicalConvertedValue!!.toFloat()))
                }

                var latestConvertedValue: Double? = null
                latestRates?.let {
                    val latestRate = it[toCurrency]
                    latestRate?.let { rate ->
                        latestConvertedValue = rate * amountValue!!
                        dataPoints.add(Entry(1f, latestConvertedValue!!.toFloat()))
                    }
                }

                val message = "Line chart exchange rates from $fromCurrency to $toCurrency\n"
                val dialog = ChartDialog(requireContext(), message, dataPoints)
                dialog.show()
            }
        }
    }

    private fun showCurrencySelectionDialog(onCurrencySelected: (String) -> Unit) {
        showCurrencyDialog(requireContext()) { selectedCurrency ->
            onCurrencySelected(selectedCurrency)
        }
    }

    private fun fetchHistoricalExchangeRate(baseCurrency: String, currencies: String, dateTo: String, onResult: (Double?) -> Unit) {
        exchangeController.fetchHistoricalExchangeRate(baseCurrency, currencies, dateTo) { exchangeRate ->
            onResult(exchangeRate)
        }
    }
}