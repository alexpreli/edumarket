package com.edumarket.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    
    
    private const val BASE_URL = "http://localhost/"

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(MockInterceptor())
        .build()

    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}
