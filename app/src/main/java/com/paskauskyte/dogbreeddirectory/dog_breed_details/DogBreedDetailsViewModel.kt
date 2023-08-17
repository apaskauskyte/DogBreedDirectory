package com.paskauskyte.dogbreeddirectory.dog_breed_details

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.paskauskyte.dogbreeddirectory.Constants.FAVORITE_ON_KEY
import com.paskauskyte.dogbreeddirectory.repository.DogBreed
import com.paskauskyte.dogbreeddirectory.repository.DogBreedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DogBreedDetailsViewModel(private val repository: DogBreedRepository) : ViewModel() {

    private lateinit var sharedPreferences: SharedPreferences

    private val _breedLiveData = MutableLiveData<DogBreed>()
    val breedLiveData: LiveData<DogBreed>
        get() = _breedLiveData

    private var _favoriteButtonStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val favoriteButtonStateFlow = _favoriteButtonStateFlow.asStateFlow()

    private val _dogImageUrlLiveData = MutableLiveData<String>()
    val dogImageUrlLiveData: LiveData<String>
        get() = _dogImageUrlLiveData

    fun assignDogBreed(dogBreed: DogBreed) {
        _breedLiveData.value = dogBreed

        viewModelScope.launch(Dispatchers.IO) {
            dogBreed.imageId?.let { getDogImageUrl(it) }
        }
    }

    fun setSharedPreferences(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }

    fun toggleDogSelectionToFavorites() {
        val dog = breedLiveData.value

        val listOfFavDogs = getFavoriteDogs().toMutableList()

        if (listOfFavDogs.contains(dog)) {
            listOfFavDogs.remove(dog)
        } else {
            if (dog != null) {
                listOfFavDogs.add(dog)
            }
        }

        val newListOfFavDogsAsString = Gson().toJson(listOfFavDogs)

        with(sharedPreferences.edit()) {
            putString(FAVORITE_ON_KEY, newListOfFavDogsAsString)
            apply()
        }

        _favoriteButtonStateFlow.value = listOfFavDogs.contains(dog)
    }

    fun getFavoriteButtonImageStateFlow() {
        val dog = breedLiveData.value
        val listOfFavDogs = getFavoriteDogs()
        _favoriteButtonStateFlow.value = listOfFavDogs.contains(dog)
    }

    private fun getFavoriteDogs(): List<DogBreed> {
        val defaultValue = emptyList<DogBreed>().toString()
        val listOfFavDogsAsString = sharedPreferences.getString(FAVORITE_ON_KEY, defaultValue)

        val listType = object : TypeToken<List<DogBreed>>() {}.type
        val favoriteDogsList = Gson().fromJson<List<DogBreed>>(listOfFavDogsAsString, listType)

        return favoriteDogsList ?: emptyList()
    }

    private suspend fun getDogImageUrl(imageId: String) {
        val image = repository.fetchDogImageUrl(imageId)
        _dogImageUrlLiveData.postValue(image.url)
    }
}