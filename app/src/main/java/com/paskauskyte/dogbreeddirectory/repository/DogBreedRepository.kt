package com.paskauskyte.dogbreeddirectory.repository

import com.paskauskyte.dogbreeddirectory.dog_api.DogApiServiceClient

class DogBreedRepository {

    suspend fun fetchDogList(): List<DogBreed> {
        val response = DogApiServiceClient.providesApiService().getDogBreeds()
        return response.body() ?: emptyList()
    }

    suspend fun fetchDogImageUrl(imageId: String): Image {
        val response = DogApiServiceClient.providesApiService().getDogBreedImage(imageId)
        return response.body() ?: Image("")
    }
}