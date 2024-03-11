package com.example.cryptocurrency_app_retrofit.service

import com.example.cryptocurrency_app_retrofit.model.ApiResponseNews
import com.example.cryptocurrency_app_retrofit.model.Article
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// https://newsapi.org/
// v2/everything?q=bitcoin&apiKey=28e2e3481e7844a7a6020a8d00a85e0e

interface NewsAPI {
    @GET("v2/everything?q=bitcoin&language=en&pageSize=50&sortBy=publishedAt&apiKey=28e2e3481e7844a7a6020a8d00a85e0e")
    fun getArticles(): Call<ApiResponseNews>
}