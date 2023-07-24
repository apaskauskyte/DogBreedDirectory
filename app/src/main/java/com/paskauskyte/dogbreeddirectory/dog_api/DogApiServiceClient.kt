package com.paskauskyte.dogbreeddirectory.dog_api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DogApiServiceClient {
    private const val BASE_URL = "https://api.thedogapi.com/"

    val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader(
                    "x-api-key",
                    "live_o1PkpE6fitbhHgmRr6nZpl1gjCA59fgOcYlPOXhfiUB935MSJ5cMTriyF4Yvt1L8"
                )
                .build()
            chain.proceed(newRequest)
        }
        .build()

    fun providesApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().create()
                )
            )
            .build()
            .create(ApiService::class.java)
    }
}