package com.nsh.currencyconverter.controllers

import com.nsh.currencyconverter.api.ExchangeAPI
import com.nsh.currencyconverter.models.CurrencyDetails
import com.nsh.currencyconverter.models.HistoricalResponse
import com.nsh.currencyconverter.models.LatestResponse
import com.nsh.currencyconverter.models.SymbolsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeController {
    fun fetchSymbols(onResult: (Map<String, CurrencyDetails>?) -> Unit) {
        ExchangeAPI.service.getSymbols().enqueue(object : Callback<SymbolsResponse> {
            override fun onResponse(call: Call<SymbolsResponse>, response: Response<SymbolsResponse>) {
                if (response.isSuccessful) {
                    onResult(response.body()?.data)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<SymbolsResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun fetchLatestExchangeRate(baseCurrency: String, currencies: String? = null, onResult: (Map<String, Double>?) -> Unit) {
        ExchangeAPI.service.getLatestExchangeRate(currencies = currencies, baseCurrency = baseCurrency).enqueue(object : Callback<LatestResponse> {
            override fun onResponse(call: Call<LatestResponse>, response: Response<LatestResponse>) {
                if (response.isSuccessful) {
                    val exchangeRates = response.body()?.data
                    onResult(exchangeRates)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<LatestResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun fetchHistoricalExchangeRate(baseCurrency: String, currencies: String, dateTo: String, onResult: (Double?) -> Unit) {
        ExchangeAPI.service.getHistoricalExchangeRate(baseCurrency = baseCurrency, currencies = currencies, dateTo = dateTo).enqueue(object : Callback<HistoricalResponse> {
            override fun onResponse(call: Call<HistoricalResponse>, response: Response<HistoricalResponse>) {
                if (response.isSuccessful) {
                    val exchangeRate = response.body()?.data?.get(dateTo)?.get(currencies)
                    onResult(exchangeRate)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<HistoricalResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }
}
