package com.nsh.currencyconverter.models

data class SymbolsResponse(
    val success: Boolean,
    val symbols: Map<String, String>
)
