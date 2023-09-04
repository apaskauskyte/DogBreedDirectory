package com.paskauskyte.dogbreeddirectory.dog_breeds.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paskauskyte.dogbreeddirectory.databinding.FragmentDogBreedsListBinding
import com.paskauskyte.dogbreeddirectory.repository.DogBreed

class DogBreedsAdapter(
    private val onClick: (DogBreed) -> Unit,
    private val onLongClick: (DogBreed) -> Boolean
) : RecyclerView.Adapter<DogBreedsViewHolder>() {

    private var dogBreedsList: List<DogBreed> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogBreedsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentDogBreedsListBinding.inflate(inflater, parent, false)
        return DogBreedsViewHolder(binding, onClick, onLongClick)
    }

    override fun onBindViewHolder(holder: DogBreedsViewHolder, position: Int) {
        val dogBreed = dogBreedsList[position]
        holder.bind(dogBreed)
    }

    override fun getItemCount(): Int {
        return dogBreedsList.size
    }

    fun setDogBreedsList(dogBreedsList: List<DogBreed>) {
        this.dogBreedsList = dogBreedsList
        notifyDataSetChanged()
    }
}