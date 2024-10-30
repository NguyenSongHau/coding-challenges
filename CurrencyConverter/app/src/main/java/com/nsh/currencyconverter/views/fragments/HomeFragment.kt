package com.nsh.currencyconverter.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nsh.currencyconverter.R
import com.nsh.currencyconverter.adapters.ConvertedCurrencyAdapter
import com.nsh.currencyconverter.controllers.ExchangeController
import com.nsh.currencyconverter.models.ConvertedCurrencyItem
import com.nsh.currencyconverter.utils.isWifiConnected
import com.nsh.currencyconverter.utils.formatCurrency
import com.nsh.currencyconverter.utils.showAlertDialog
import com.nsh.currencyconverter.utils.showCurrencyDialog
import com.nsh.currencyconverter.utils.hideKeyboard
import com.nsh.currencyconverter.utils.showKeyboard

class HomeFragment : Fragment() {
    private lateinit var convertFromDropDown: TextView
    private lateinit var convertToDropDown: TextView
    private lateinit var amount: EditText
    private lateinit var result: TextView
    private lateinit var exchangeButton: Button
    private lateinit var currencyGridView: GridView
    private lateinit var currencyGridAdapter: ConvertedCurrencyAdapter
    private val exchangeController = ExchangeController()

    private var fromCurrency: String = ""
    private var toCurrency: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        requireActivity().actionBar?.hide()

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        convertFromDropDown = view.findViewById(R.id.convertFromDropDown)
        convertToDropDown = view.findViewById(R.id.convertToDropDown)
        amount = view.findViewById(R.id.amount)
        result = view.findViewById(R.id.result)
        exchangeButton = view.findViewById(R.id.exchangeButton)
        currencyGridView = view.findViewById(R.id.currencyGridView)

        currencyGridAdapter = ConvertedCurrencyAdapter(requireContext(), emptyList(), 1.0)
        currencyGridView.adapter = currencyGridAdapter

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

        exchangeButton.setOnClickListener {
            convertCurrency()
        }

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

    private fun showCurrencySelectionDialog(onCurrencySelected: (String) -> Unit) {
        showCurrencyDialog(requireContext()) { selectedCurrency ->
            onCurrencySelected(selectedCurrency)
        }
    }

    private fun convertCurrency() {
        val amountValue = amount.text.toString().toDoubleOrNull()

        if (!isWifiConnected(requireContext())) {
            showAlertDialog(requireContext(), "Network Error", "No WiFi connection. Please check your network settings.")
            return
        }

        if (fromCurrency.isEmpty() || toCurrency.isEmpty() || amountValue == null) {
            showAlertDialog(requireContext(), "Error", "Please enter a valid amount and select currencies!")
            return
        }

        exchangeController.fetchLatestExchangeRate(baseCurrency = fromCurrency) { rates ->
            if (rates != null) {
                val exchangeRate = rates[toCurrency]
                if (exchangeRate != null) {
                    val resultValue = amountValue * exchangeRate

                    val formattedAmount = formatCurrency(amountValue)
                    val formattedResult = formatCurrency(resultValue)

                    result.text = "$formattedAmount $fromCurrency = $formattedResult $toCurrency"

                    fetchLatestExchangeRates(amountValue)
                } else {
                    result.text = "Exchange rate not found"
                }
            } else {
                result.text = "Error fetching rate"
            }
        }
    }

    private fun fetchLatestExchangeRates(amountValue: Double) {
        exchangeController.fetchLatestExchangeRate(baseCurrency = fromCurrency) { rates ->
            if (rates != null) {
                val convertedItems = rates.map { (currency, rate) ->
                    ConvertedCurrencyItem(currency, rate.toString())
                }
                currencyGridAdapter.updateData(convertedItems, amountValue)
            } else {
                result.text = "Error fetching latest exchange rates!"
            }
        }
    }
}