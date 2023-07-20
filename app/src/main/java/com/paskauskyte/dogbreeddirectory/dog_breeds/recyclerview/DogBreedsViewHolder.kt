package com.paskauskyte.dogbreeddirectory.dog_breeds.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.paskauskyte.dogbreeddirectory.databinding.FragmentDogBreedsListBinding
import com.paskauskyte.dogbreeddirectory.dog_breeds.DogBreed

class DogBreedsViewHolder(
    private val binding: FragmentDogBreedsListBinding,
    private val onClick: (DogBreed) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var currentDogBreed: DogBreed? = null

    init {
        binding.root.setOnClickListener { currentDogBreed?.let { dogBreed -> onClick(dogBreed) } }
    }

    fun bind(dogBreed: DogBreed) {
        currentDogBreed = dogBreed
        binding.dogBreedTextView.text = dogBreed.dogBreedName
    }
}