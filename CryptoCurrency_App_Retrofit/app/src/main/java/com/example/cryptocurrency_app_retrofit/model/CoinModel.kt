package com.example.cryptocurrency_app_retrofit.model

data class CoinModelItem(
    val age: Int,
    val allTimeHighUSD: Double,
    val cap: Double,
    val categories: List<String>,
    val circulatingSupply: Double,
    val code: String,
    val color: String,
    val delta: Delta,
    val exchanges: Int,
    val links: Links,
    val markets: Int,
    val maxSupply: Double,
    val name: String,
    val pairs: Int,
    val png32: String,
    val png64: String,
    val rank: Int,
    val rate: Double,
    val symbol: String,
    val totalSupply: Double,
    val volume: Double,
    val webp32: String,
    val webp64: String,

)

data class Delta(
    val day: Double,
    val hour: Double,
    val month: Double,
    val quarter: Double,
    val week: Double,
    val year: Double
)

data class Links(
    val discord: String,
    val instagram: String,
    val linkedin: String,
    val medium: String,
    val naver: Any,
    val reddit: String,
    val soundcloud: Any,
    val spotify: Any,
    val telegram: String,
    val tiktok: String,
    val twitch: String,
    val twitter: String,
    val website: String,
    val wechat: String,
    val whitepaper: String,
    val youtube: String
)

data class CoinListRequest(
    val currency: String,
    val sort: String,
    val order: String,
    val offset: Int,
    val limit: Int,
    val meta: Boolean
)
