package com.nsh.currencyconverter.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.nsh.currencyconverter.models.LatestResponse
import com.nsh.currencyconverter.models.SymbolsResponse
import com.nsh.currencyconverter.models.HistoricalResponse

object ExchangeAPI {
    private const val BASE_URL = "https://api.freecurrencyapi.com/v1/"
    private const val API_KEY = "fca_live_Ih2y8xSFAXUSW68vwlBHh1CkmkAsW0xFQD2tavjF"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    interface ExchangeApiService {
        @GET("currencies")
        fun getSymbols(@Query("apikey") apiKey: String = API_KEY): Call<SymbolsResponse>

        @GET("latest")
        fun getLatestExchangeRate(
            @Query("apikey") apiKey: String = API_KEY,
            @Query("base_currency") baseCurrency: String,
            @Query("currencies") currencies: String? = null
        ): Call<LatestResponse>

        @GET("historical")
        fun getHistoricalExchangeRate(
            @Query("apikey") apiKey: String = API_KEY,
            @Query("base_currency") baseCurrency: String,
            @Query("currencies") currencies: String,
            @Query("date_to") dateTo: String
        ): Call<HistoricalResponse>
    }

    val service: ExchangeApiService by lazy {
        retrofit.create(ExchangeApiService::class.java)
    }
}
