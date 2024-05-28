package com.zacker.bookmaster.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BookClient {

    private const val BASE_URL = "http://10.0.2.2:5001"

    operator fun invoke(): ApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().also { client ->
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
            }.build())
            .build()
            .create(ApiService::class.java)
}