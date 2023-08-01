package com.paskauskyte.dogbreeddirectory.dog_breed_details

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.paskauskyte.dogbreeddirectory.dog_breeds.DogBreed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DogBreedDetailsViewModel : ViewModel() {

    private lateinit var sharedPreferences: SharedPreferences

    private val _breedLiveData = MutableLiveData<DogBreed>()
    val breedLiveData: LiveData<DogBreed>
        get() = _breedLiveData

    private var _favoriteButtonStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val favoriteButtonStateFlow = _favoriteButtonStateFlow.asStateFlow()

    fun saveDogBreed(breed: DogBreed) {
        _breedLiveData.value = breed
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
            putString("key_favorite_on", newListOfFavDogsAsString)
            apply()
        }

        updateFavoriteButtonImageStateFlow()
    }

    private fun updateFavoriteButtonImageStateFlow() {
        _favoriteButtonStateFlow.value = !_favoriteButtonStateFlow.value
    }

    fun getFavoriteButtonImageStateFlow() {
        val dog = breedLiveData.value
        val listOfFavDogs = getFavoriteDogs()
        _favoriteButtonStateFlow.value = listOfFavDogs.contains(dog)
    }

    private fun getFavoriteDogs(): List<DogBreed> {
        val defaultValue = emptyList<DogBreed>().toString()
        val listOfFavDogsAsString =
            sharedPreferences.getString("key_favorite_on", defaultValue)

        val listType = object : TypeToken<List<DogBreed>>() {}.type
        return Gson().fromJson(listOfFavDogsAsString, listType)
    }
}