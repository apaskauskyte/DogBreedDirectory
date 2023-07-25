package com.paskauskyte.dogbreeddirectory.dog_breeds

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.paskauskyte.dogbreeddirectory.MainActivity
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

        viewModel.fetchDogBreeds()
        setUpRecyclerView()
        observeDogBreedStateFlow()
        setupSearchView()
    }

    private fun setUpRecyclerView() {
        binding.dogBreedsRecyclerView.apply {
            recyclerAdapter = DogBreedsAdapter { dogBreed -> onDogBreedCLick(dogBreed) }
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun onDogBreedCLick(dogBreed: DogBreed) {
        (activity as MainActivity).openDogBreedDetails()
        transferDataToDogBreedDetailsFragment(dogBreed)
    }

    private fun observeDogBreedStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.dogBreedsStateFlow.collect { response ->

                    if (response != null) {
                        submitDogBreeds(response)
                    }
                }
            }
        }
    }

    private fun submitDogBreeds(list: List<DogBreed>) {
        recyclerAdapter?.submitList(list)
        binding.dogBreedsRecyclerView.adapter = recyclerAdapter
    }

    private fun setupSearchView() {
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterDogBreedList(binding.searchView.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
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

    private fun transferDataToDogBreedDetailsFragment(dogBreed: DogBreed) {
        val bundle = bundleOf(KEY_DOG_BREED to dogBreed)
        setFragmentResult(REQUEST_KEY_DOG_BREED, bundle)
    }

    companion object {
        const val TAG = "dog_breeds_fragment"
        const val REQUEST_KEY_DOG_BREED = "breed_fragment_result_key"
        const val KEY_DOG_BREED = "key_breed"
        fun newInstance() = DogBreedsFragment()
    }
}