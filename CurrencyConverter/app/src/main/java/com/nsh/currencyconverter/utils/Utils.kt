package com.nsh.currencyconverter.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun isWifiConnected(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
