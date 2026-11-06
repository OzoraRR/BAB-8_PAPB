package com.example.navigationlab.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://dummyjson.com/"
    val apiService: StudentApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder()
                .connectTimeout(12, TimeUnit.SECONDS)
                .readTimeout(12, TimeUnit.SECONDS)
                .callTimeout(20, TimeUnit.SECONDS)
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StudentApiService::class.java)
    }
}
