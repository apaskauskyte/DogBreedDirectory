package com.paskauskyte.dogbreeddirectory.dog_breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paskauskyte.dogbreeddirectory.repository.DogBreed
import com.paskauskyte.dogbreeddirectory.repository.DogBreedRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DogBreedsViewModel(
    private val repository: DogBreedRepository,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _dogBreedsStateFlow: MutableStateFlow<List<DogBreed>> =
        MutableStateFlow(mutableListOf())
    val dogBreedsStateFlow = _dogBreedsStateFlow.asStateFlow()

    private var dogList: List<DogBreed> = emptyList()

    private var sortMode: SortMode = SortMode.AZ

    init {
        viewModelScope.launch(ioDispatcher) {
            getDogList()
        }
    }

    private suspend fun getDogList() {
        dogList = repository.fetchDogList()
        _dogBreedsStateFlow.value = getSortedDogList()
    }

    fun setSortingMode(sortMode: SortMode) {
        if (sortMode != this.sortMode) {
            this.sortMode = sortMode
        }
        _dogBreedsStateFlow.value = getSortedDogList()
    }

    private fun getSortedDogList(): List<DogBreed> {
        val sortedDogList = when (sortMode) {
            SortMode.AZ -> this.dogList.sortedBy { it.name }
            SortMode.ZA -> this.dogList.sortedByDescending { it.name }
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

    fun getCurrentSortingMode(): SortMode {
        return sortMode
    }

    enum class SortMode {
        AZ, ZA
    }
}