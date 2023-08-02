package com.paskauskyte.dogbreeddirectory.dog_breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paskauskyte.dogbreeddirectory.dog_api.DogApiServiceClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DogBreedsViewModel : ViewModel() {

    private val _dogBreedsStateFlow: MutableStateFlow<List<DogBreed>> =
        MutableStateFlow(mutableListOf())
    val dogBreedsStateFlow = _dogBreedsStateFlow.asStateFlow()

    private var dogList: List<DogBreed> = emptyList()

    private var sortMode: SortMode = SortMode.AZ

    fun setSortingMode(sortMode: SortMode) {
        if (sortMode != this.sortMode) {
            this.sortMode = sortMode
            _dogBreedsStateFlow.value = sortDogList()
        }
    }

    fun fetchDogBreeds() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = DogApiServiceClient.providesApiService().getDogBreeds()
            _dogBreedsStateFlow.value = response.body() ?: emptyList()
            _dogBreedsStateFlow.value = sortDogList()
            dogList = _dogBreedsStateFlow.value
        }
    }

    private fun sortDogList(): List<DogBreed> {
        val sortedDogList = when (sortMode) {
            SortMode.AZ -> _dogBreedsStateFlow.value.sortedBy { it.name }
            SortMode.ZA -> _dogBreedsStateFlow.value.sortedByDescending { it.name }
        }
        return sortedDogList
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

    enum class SortMode {
        AZ, ZA
    }
}