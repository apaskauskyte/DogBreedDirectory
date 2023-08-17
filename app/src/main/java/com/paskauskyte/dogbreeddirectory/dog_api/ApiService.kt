package com.paskauskyte.dogbreeddirectory.dog_api

import com.paskauskyte.dogbreeddirectory.repository.DogBreed
import com.paskauskyte.dogbreeddirectory.repository.Image
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("v1/breeds")
    suspend fun getDogBreeds(): Response<List<DogBreed>>

    @GET("v1/images/{reference_image_id}")
    suspend fun getDogBreedImage(@Path("reference_image_id") imageId: String): Response<Image>
}