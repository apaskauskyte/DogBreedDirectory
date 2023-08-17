package com.paskauskyte.dogbreeddirectory.dog_breed_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.paskauskyte.dogbreeddirectory.repository.DogBreedRepository

class DogBreedDetailsViewModelFactory(private val repository: DogBreedRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(DogBreedDetailsViewModel::class.java)) {
            return DogBreedDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}