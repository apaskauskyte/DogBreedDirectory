package com.paskauskyte.dogbreeddirectory.dog_breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paskauskyte.dogbreeddirectory.dog_api.DogApiServiceClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DogBreedsViewModel : ViewModel() {

    private val dogBreeds = listOf<DogBreed>()

    private val _dogBreedsStateFlow: MutableStateFlow<List<DogBreed>?> =
        MutableStateFlow(dogBreeds)
    val dogBreedsStateFlow = _dogBreedsStateFlow.asStateFlow()

    fun fetchDogBreeds() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = DogApiServiceClient.providesApiService().getDogBreeds()
            _dogBreedsStateFlow.value = response.body()
        }
    }
}