package com.joe.yhfinance.api

import com.joe.yhfinance.constants.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        val newRequest: Request = originalRequest.newBuilder()
            .header("X-RapidAPI-Key", Constants.key_rapid_api)
            .header("X-RapidAPI-Host", Constants.host_rapid_api)
            .build()

        return chain.proceed(newRequest)
    }
}