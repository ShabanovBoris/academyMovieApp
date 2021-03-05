package com.example.academyhomework.domain.data

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder()
            .url(original.url.toString().plus(apiKey))
            .build()

        return chain.proceed(request)
    }

    companion object {
        private const val apiKey = "&api_key=4638f9d08c5772da23d03dc27501af71"
    }
}

