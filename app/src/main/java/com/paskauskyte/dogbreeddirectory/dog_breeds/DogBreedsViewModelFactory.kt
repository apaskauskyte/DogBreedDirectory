package com.paskauskyte.dogbreeddirectory.dog_breeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.paskauskyte.dogbreeddirectory.repository.DogBreedRepository

class DogBreedsViewModelFactory(private val repository: DogBreedRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(DogBreedsViewModel::class.java)) {
            return DogBreedsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}