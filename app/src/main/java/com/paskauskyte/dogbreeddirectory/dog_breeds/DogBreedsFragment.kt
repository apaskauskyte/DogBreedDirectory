package com.paskauskyte.dogbreeddirectory.dog_breeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.paskauskyte.dogbreeddirectory.databinding.FragmentDogBreedsBinding

class DogBreedsFragment : Fragment() {

    private val viewModel: DogBreedsViewModel by viewModels()

    private var _binding: FragmentDogBreedsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDogBreedsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = DogBreedsFragment()
    }
}