package com.paskauskyte.dogbreeddirectory.dog_breeds

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DogBreedsViewModel : ViewModel() {

    private val _dogBreedsStateFlow: MutableStateFlow<List<DogBreed>> =
        MutableStateFlow(emptyList())

    val dogBreedsStateFlow = _dogBreedsStateFlow.asStateFlow()

    init {
        generateDogBreeds()
    }

    fun generateDogBreeds() {
        val dogBreedsList = mutableListOf<DogBreed>()
        for (i in 1..20) {
            val dogBreedName = generateRandomDogBreed()
            val dogBreed = DogBreed(dogBreedName)
            dogBreedsList.add(dogBreed)
        }

        _dogBreedsStateFlow.value = dogBreedsList
    }

    private fun generateRandomDogBreed(): String {
        val randomNumber = (0..100).random()
        return "dogBreed$randomNumber"
    }
}