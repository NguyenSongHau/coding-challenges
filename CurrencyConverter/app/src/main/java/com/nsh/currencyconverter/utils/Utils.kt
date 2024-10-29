package com.nsh.currencyconverter.utils

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ListView
import com.nsh.currencyconverter.adapters.CurrencyAdapter
import com.nsh.currencyconverter.controllers.ExchangeController
import com.nsh.currencyconverter.R
import com.nsh.currencyconverter.models.CurrencyDetails
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun isWifiConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
    return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
}

fun formatCurrency(value: Double): String {
    val decimalFormat = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))
    return decimalFormat.format(value)
}

fun formatDate(date: Date, pattern: String = "yyyy-MM-dd"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(date)
}

fun showCurrencyDialog(
    context: Context,
    onCurrencySelected: (String) -> Unit
) {
    val dialog = Dialog(context)
    dialog.setContentView(R.layout.spinner)

    val lvCurrency = dialog.findViewById<ListView>(R.id.lvCurrency)
    val searchEditText = dialog.findViewById<EditText>(R.id.edtCurrency)

    val exchangeController = ExchangeController()
    exchangeController.fetchSymbols { symbols ->
        symbols?.let {
            val currencyDetailsList = it.map { entry -> entry.value }.toMutableList()
            val currencyCodes = it.keys.toList()

            val filteredCurrencyDetails = mutableListOf<CurrencyDetails>()
            val adapter = CurrencyAdapter(context, filteredCurrencyDetails)
            lvCurrency.adapter = adapter

            // Initially display all currencies
            filteredCurrencyDetails.addAll(currencyDetailsList)
            adapter.notifyDataSetChanged()

            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val query = s.toString().lowercase()
                    filteredCurrencyDetails.clear()
                    filteredCurrencyDetails.addAll(currencyDetailsList.filter { it.name.lowercase().contains(query) })

                    adapter.notifyDataSetChanged()
                }
            })

            lvCurrency.setOnItemClickListener { _, _, position, _ ->
                val selectedCurrencyCode = currencyCodes[currencyDetailsList.indexOf(adapter.getItem(position))]
                onCurrencySelected(selectedCurrencyCode)
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}

