package com.paskauskyte.dogbreeddirectory.dog_breeds.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.paskauskyte.dogbreeddirectory.databinding.FragmentDogBreedsListBinding
import com.paskauskyte.dogbreeddirectory.repository.DogBreed

class DogBreedsViewHolder(
    private val binding: FragmentDogBreedsListBinding,
    private val onClick: (DogBreed) -> Unit,
    private val onLongClick: (DogBreed) -> Boolean
) : RecyclerView.ViewHolder(binding.root) {
    private var currentDogBreed: DogBreed? = null

    init {
        binding.root.setOnClickListener {
            currentDogBreed?.let { dogBreed -> onClick(dogBreed) }
        }
        binding.root.setOnLongClickListener {
            currentDogBreed?.let { dogBreed -> onLongClick(dogBreed) }
                ?: false
        }
    }

    fun bind(dogBreed: DogBreed) {
        currentDogBreed = dogBreed
        binding.dogBreedTextView.text = dogBreed.name
    }
}