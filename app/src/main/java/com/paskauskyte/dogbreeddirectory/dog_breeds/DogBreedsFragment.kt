package com.paskauskyte.dogbreeddirectory.dog_breeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.paskauskyte.dogbreeddirectory.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_menu, menu)
    }

    companion object {
        fun newInstance() = DogBreedsFragment()
    }
}