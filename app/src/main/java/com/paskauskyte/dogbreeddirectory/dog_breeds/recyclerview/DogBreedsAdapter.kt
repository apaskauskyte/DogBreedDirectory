package com.paskauskyte.dogbreeddirectory.dog_breeds.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.paskauskyte.dogbreeddirectory.databinding.FragmentDogBreedsListBinding
import com.paskauskyte.dogbreeddirectory.dog_breeds.DogBreed

class DogBreedsAdapter(
    private val onClick: (DogBreed) -> Unit
) : ListAdapter<DogBreed, DogBreedsViewHolder>(
    Comparator()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DogBreedsViewHolder(
            FragmentDogBreedsListBinding
                .inflate(LayoutInflater.from(parent.context), parent, false),
            onClick
        )

    override fun onBindViewHolder(holder: DogBreedsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class Comparator : DiffUtil.ItemCallback<DogBreed>() {
        override fun areItemsTheSame(oldItem: DogBreed, newItem: DogBreed) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: DogBreed, newItem: DogBreed) =
            oldItem == newItem
    }
}