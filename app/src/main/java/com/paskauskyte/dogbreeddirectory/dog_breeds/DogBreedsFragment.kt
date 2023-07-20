package com.paskauskyte.dogbreeddirectory.dog_breeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.paskauskyte.dogbreeddirectory.R
import com.paskauskyte.dogbreeddirectory.databinding.FragmentDogBreedsBinding
import com.paskauskyte.dogbreeddirectory.dog_breeds.recyclerview.DogBreedsAdapter
import kotlinx.coroutines.launch

class DogBreedsFragment : Fragment() {

    private val viewModel: DogBreedsViewModel by viewModels()
    private var recyclerAdapter: DogBreedsAdapter? = null

    private var _binding: FragmentDogBreedsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDogBreedsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        addDogBreedsList()
    }

    private fun setUpRecyclerView() {
        binding.dogBreedsRecyclerView.apply {
            recyclerAdapter = DogBreedsAdapter { dogBreed -> }
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun addDogBreedsList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.dogBreedsStateFlow.collect { dogBreeds ->
                    submitDogBreeds(dogBreeds)
                }
            }
        }
    }

    private fun submitDogBreeds(list: List<DogBreed>) {
        recyclerAdapter?.submitList(list)
        binding.dogBreedsRecyclerView.adapter = recyclerAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "dog_breeds_fragment"
        fun newInstance() = DogBreedsFragment()
    }
}