package com.paskauskyte.dogbreeddirectory.dog_api

import com.paskauskyte.dogbreeddirectory.repository.DogBreed
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("v1/breeds")
    suspend fun getDogBreeds(): Response<List<DogBreed>>
}