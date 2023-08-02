package com.paskauskyte.dogbreeddirectory.dog_of_the_day

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paskauskyte.dogbreeddirectory.dog_api.DogApiServiceClient
import com.paskauskyte.dogbreeddirectory.dog_breeds.DogBreed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Random

class DogOfTheDayViewModel : ViewModel() {

    private val _dogOfTheDayLiveData = MutableLiveData<DogBreed>()
    val dogOfTheDayLiveData: MutableLiveData<DogBreed>
        get() = _dogOfTheDayLiveData

    private var dogList: List<DogBreed> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchAllDogs()
            getDogOfTheDay()
        }
    }

    private suspend fun fetchAllDogs() {
        val response = DogApiServiceClient.providesApiService().getDogBreeds()
        dogList = response.body() ?: emptyList()
    }

    private fun getDogOfTheDay() {
        if (dogList.isNotEmpty()) {
            val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            val randomNumber = Random(today.toLong())
            val randomIndex = randomNumber.nextInt(dogList.size)

            _dogOfTheDayLiveData.postValue(dogList[randomIndex])
        }
    }
}