package com.paskauskyte.dogbreeddirectory.dog_breed_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.paskauskyte.dogbreeddirectory.databinding.FragmentDogBreedDetailsBinding

class DogBreedDetailsFragment : Fragment() {

    private val viewModel: DogBreedDetailsViewModel by viewModels()

    private var _binding: FragmentDogBreedDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDogBreedDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "dog_breed_details_fragment"
        fun newInstance() = DogBreedDetailsFragment()
    }
}