package com.paskauskyte.dogbreeddirectory.favorites.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.paskauskyte.dogbreeddirectory.databinding.FragmentDogBreedsListBinding
import com.paskauskyte.dogbreeddirectory.dog_breeds.DogBreed

class FavoritesViewHolder(
    private val binding: FragmentDogBreedsListBinding,
    private val onClick: (DogBreed) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var currentDogBreed: DogBreed? = null

    init {
        binding.root.setOnClickListener { currentDogBreed?.let { breed -> onClick(breed) } }
    }

    fun bind(breed: DogBreed) {
        currentDogBreed = breed
        binding.dogBreedTextView.text = breed.name
    }
}