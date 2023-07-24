package com.paskauskyte.dogbreeddirectory.dog_breed_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paskauskyte.dogbreeddirectory.dog_breeds.DogBreed

class DogBreedDetailsViewModel : ViewModel() {

    private val _breedLiveData = MutableLiveData<DogBreed>()
    val breedLiveData: LiveData<DogBreed>
        get() = _breedLiveData

    fun saveDogBreed(breed: DogBreed) {
        _breedLiveData.value = breed
    }
}