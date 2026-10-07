package com.pemmob.responsi1pemmob.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://api.tenrai.org/v1/"
    private const val FALLBACK_HOST = "api.jikan.moe"
    private const val FALLBACK_PREFIX = "/v4"

    private val fallbackInterceptor = Interceptor { chain ->
        val request = chain.request()
        try {
            chain.proceed(request)
        } catch (e: IOException) {
            val originalUrl = request.url
            if (originalUrl.host == "api.tenrai.org") {
                val currentPath = originalUrl.encodedPath
                val newPath = if (currentPath.startsWith("/v1")) {
                    FALLBACK_PREFIX + currentPath.removePrefix("/v1")
                } else {
                    FALLBACK_PREFIX + currentPath
                }

                val newUrl = originalUrl.newBuilder()
                    .scheme("https")
                    .host(FALLBACK_HOST)
                    .encodedPath(newPath)
                    .build()

                val fallbackRequest = request.newBuilder()
                    .url(newUrl)
                    .build()

                chain.proceed(fallbackRequest)
            } else {
                throw e
            }
        }
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(fallbackInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val apiService: TenraiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TenraiApiService::class.java)
    }
}
