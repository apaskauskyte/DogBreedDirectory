package com.paskauskyte.dogbreeddirectory.dog_breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paskauskyte.dogbreeddirectory.dog_api.DogApiServiceClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DogBreedsViewModel : ViewModel() {

    private val _dogBreedsStateFlow: MutableStateFlow<List<DogBreed>?> =
        MutableStateFlow(mutableListOf())
    val dogBreedsStateFlow = _dogBreedsStateFlow.asStateFlow()

    private var dogList: List<DogBreed> = emptyList()

    fun fetchDogBreeds() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = DogApiServiceClient.providesApiService().getDogBreeds()
            _dogBreedsStateFlow.value = response.body()
            dogList = _dogBreedsStateFlow.value!!
        }
    }

    fun filterDogBreedList(enteredText: String) {
        val filteredList = mutableListOf<DogBreed>()
        dogList.forEach {
            if (it.name.contains(enteredText, true)) {
                filteredList.add(it)
            }
        }
        _dogBreedsStateFlow.value = filteredList
    }
}