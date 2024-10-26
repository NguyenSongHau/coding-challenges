package com.nsh.currencyconverter.controllers

import com.nsh.currencyconverter.api.ExchangeAPI
import com.nsh.currencyconverter.models.SymbolsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeController {
    fun fetchSymbols(onResult: (Map<String, String>?) -> Unit) {
        ExchangeAPI.service.getSymbols().enqueue(object : Callback<SymbolsResponse> {
            override fun onResponse(call: Call<SymbolsResponse>, response: Response<SymbolsResponse>) {
                if (response.isSuccessful) {
                    onResult(response.body()?.symbols)
                } else {
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<SymbolsResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }
}
