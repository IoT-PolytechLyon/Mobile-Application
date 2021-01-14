package com.polytech.bmh.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {
    companion object {
        //private cons val BASE_URL: String = "http://192.168.0.55:8081"
        private const val BASE_URL: String = "http://10.0.2.2:8081"

        fun getRetrofitInstance(): Retrofit {
            val client:OkHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }


    }
}
