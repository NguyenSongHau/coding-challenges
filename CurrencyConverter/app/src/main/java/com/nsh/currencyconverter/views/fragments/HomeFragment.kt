package com.nsh.currencyconverter.views.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nsh.currencyconverter.R
import com.nsh.currencyconverter.adapters.ConvertedCurrencyAdapter
import com.nsh.currencyconverter.adapters.CurrencyAdapter
import com.nsh.currencyconverter.controllers.ExchangeController
import com.nsh.currencyconverter.models.ConvertedCurrencyItem
import com.nsh.currencyconverter.models.CurrencyDetails
import com.nsh.currencyconverter.utils.isWifiConnected
import com.nsh.currencyconverter.utils.formatCurrency

class HomeFragment : Fragment() {
    private lateinit var convertFromDropDown: TextView
    private lateinit var convertToDropDown: TextView
    private lateinit var amount: EditText
    private lateinit var result: TextView
    private lateinit var exchangeButton: Button
    private lateinit var currencyGridView: GridView
    private lateinit var currencyGridAdapter: ConvertedCurrencyAdapter
    private val exchangeController = ExchangeController()
    private var filteredCurrencyDetails = listOf<CurrencyDetails>()

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
            showCurrencyDialog { selectedCurrency -> convertFromDropDown.text = selectedCurrency }
        }

        convertToDropDown.setOnClickListener {
            showCurrencyDialog { selectedCurrency -> convertToDropDown.text = selectedCurrency }
        }

        exchangeButton.setOnClickListener {
            convertCurrency()
        }

        return view
    }

    private fun convertCurrency() {
        val fromCurrency = convertFromDropDown.text.toString()
        val toCurrency = convertToDropDown.text.toString()
        val amountValue = amount.text.toString().toDoubleOrNull()

        if (!isWifiConnected(requireContext())) {
            AlertDialog.Builder(requireContext())
                .setTitle("Network Error")
                .setMessage("No WiFi connection. Please check your network settings.")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
            return
        }

        if (fromCurrency.isEmpty() || toCurrency.isEmpty() || amountValue == null) {
            AlertDialog.Builder(requireContext())
                .setTitle("Error")
                .setMessage("Please enter a valid amount and select currencies!")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
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
        exchangeController.fetchLatestExchangeRate(baseCurrency = convertFromDropDown.text.toString()) { rates ->
            if (rates != null) {
                val convertedItems = rates.map { (currency, rate) ->
                    ConvertedCurrencyItem(currency, rate.toString())
                }
                currencyGridAdapter.updateData(convertedItems, amountValue)
            } else {
                result.text = "Error fetching latesat exchange rates!"
            }
        }
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
}