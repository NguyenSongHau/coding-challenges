package com.nsh.currencyconverter.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.nsh.currencyconverter.models.SymbolsResponse

object ExchangeAPI {
    private const val BASE_URL = "https://api.exchangeratesapi.io/v1/"
    private const val API_KEY = "6ef4edc9ed0f2b96dcbb28b229a6b779"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    interface ExchangeApiService {
        @GET("symbols")
        fun getSymbols(@Query("access_key") apiKey: String = API_KEY): Call<SymbolsResponse>
    }

    val service: ExchangeApiService by lazy {
        retrofit.create(ExchangeApiService::class.java)
    }
}
