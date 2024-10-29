package com.nsh.currencyconverter.models

data class CurrencyDetails(
    val symbol: String,
    val name: String,
    val symbol_native: String,
    val decimal_digits: Int,
    val rounding: Int,
    val code: String,
    val name_plural: String,
    val type: String
)

data class SymbolsResponse(
    val data: Map<String, CurrencyDetails>
)

data class LatestResponse(
    val data: Map<String, Double>
)

data class ConvertedCurrencyItem(
    val name: String,
    val value: String
)