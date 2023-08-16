package com.paskauskyte.dogbreeddirectory.dog_breed_details

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.paskauskyte.dogbreeddirectory.Constants.FAVORITE_ON_KEY
import com.paskauskyte.dogbreeddirectory.repository.DogBreed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DogBreedDetailsViewModel : ViewModel() {

    private lateinit var sharedPreferences: SharedPreferences

    private val _breedLiveData = MutableLiveData<DogBreed>()
    val breedLiveData: LiveData<DogBreed>
        get() = _breedLiveData

    private var _favoriteButtonStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val favoriteButtonStateFlow = _favoriteButtonStateFlow.asStateFlow()

    fun assignDogBreed(dogBreed: DogBreed) {
        _breedLiveData.value = dogBreed
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

    fun getDogImageUrl(): String {
        return "https://cdn2.thedogapi.com/images/" + breedLiveData.value?.imageId + ".jpg"
    }
}