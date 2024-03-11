package com.example.cryptocurrency_app_retrofit.model

import com.example.cryptocurrency_app_retrofit.service.CryptoAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiCoinClient {
    private const val BASE_URL = "https://api.livecoinwatch.com/"

    val cryptoService: CryptoAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoAPI::class.java)
    }
}