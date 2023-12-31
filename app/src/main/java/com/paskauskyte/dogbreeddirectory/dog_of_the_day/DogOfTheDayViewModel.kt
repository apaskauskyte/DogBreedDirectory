package com.paskauskyte.dogbreeddirectory.dog_of_the_day

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paskauskyte.dogbreeddirectory.repository.DogBreed
import com.paskauskyte.dogbreeddirectory.repository.DogBreedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Random

class DogOfTheDayViewModel(private val repository: DogBreedRepository) : ViewModel() {

    private val _dogOfTheDayLiveData = MutableLiveData<DogBreed>()
    val dogOfTheDayLiveData: MutableLiveData<DogBreed>
        get() = _dogOfTheDayLiveData

    private var dogList: List<DogBreed> = emptyList()

    private val _loadingSpinnerLiveData = MutableLiveData<Boolean>()
    val loadingSpinnerLiveData: MutableLiveData<Boolean>
        get() = _loadingSpinnerLiveData

    private val _dogImageUrlLiveData = MutableLiveData<String?>()
    val dogImageUrlLiveData: MutableLiveData<String?>
        get() = _dogImageUrlLiveData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getDogList()
            getDogOfTheDay()
        }
    }

    private suspend fun getDogList() {
        _loadingSpinnerLiveData.postValue(true)
        dogList = repository.fetchDogList()
        _loadingSpinnerLiveData.postValue(false)
    }

    private fun getDogOfTheDay() {
        if (dogList.isNotEmpty()) {
            val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            val randomNumber = Random(today.toLong())
            val randomIndex = randomNumber.nextInt(dogList.size)
            val dogOfTheDay = dogList[randomIndex]

            viewModelScope.launch(Dispatchers.IO) {
                dogOfTheDay.imageId?.let { getDogImageUrl(it) }
            }

            _dogOfTheDayLiveData.postValue(dogOfTheDay)
        }
    }

    private suspend fun getDogImageUrl(imageId: String) {
        val image = repository.fetchDogImageUrl(imageId)
        _dogImageUrlLiveData.postValue(image.url)
    }
}