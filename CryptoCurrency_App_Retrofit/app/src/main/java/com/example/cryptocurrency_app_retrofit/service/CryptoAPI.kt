package com.example.cryptocurrency_app_retrofit.service

import com.example.cryptocurrency_app_retrofit.model.CoinListRequest
import com.example.cryptocurrency_app_retrofit.model.CoinModelItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CryptoAPI {
    @Headers(
        "content-type: application/json",
        "x-api-key: cd68accb-d543-4bc0-bdda-3b5b76d46add"
    )

    @POST("coins/list")
    fun getCoins(@Body request: CoinListRequest): Call<List<CoinModelItem>>

}