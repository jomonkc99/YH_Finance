package com.joe.yhfinance.api

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("market/v2/get-summary?region=US")
    suspend fun getItems(): Response<JsonObject>

    @GET("stock/v2/get-summary")
    suspend fun getDetails(@Query("symbol") symbol: String): Response<JsonObject>

}
