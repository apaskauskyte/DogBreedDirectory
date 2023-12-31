package com.paskauskyte.dogbreeddirectory.dog_breeds

import android.content.Context
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
import com.paskauskyte.dogbreeddirectory.Constants.SORT_AZ_ON_KEY
import com.paskauskyte.dogbreeddirectory.Constants.SORT_MODE_SHARED_PREFS_NAME
import com.paskauskyte.dogbreeddirectory.MainActivity
import com.paskauskyte.dogbreeddirectory.R
import com.paskauskyte.dogbreeddirectory.databinding.FragmentDogBreedsBinding
import com.paskauskyte.dogbreeddirectory.dog_breeds.recyclerview.DogBreedsAdapter
import com.paskauskyte.dogbreeddirectory.repository.DogBreed
import com.paskauskyte.dogbreeddirectory.repository.DogBreedRepository
import kotlinx.coroutines.launch

class DogBreedsFragment : Fragment() {

    private val viewModel: DogBreedsViewModel by viewModels {
        DogBreedsViewModelFactory(DogBreedRepository())
    }

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

        viewModel.setSortingMode(getSharedPref())
        setUpRecyclerView()
        observeDogBreedStateFlow()
        setUpSearchView()
        onDogOfTheDayButtonClick()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setSortingMode(getSharedPref())
    }

    private fun getSharedPref(): DogBreedsViewModel.SortMode {
        val sharedPrefs = requireActivity().getSharedPreferences(
            SORT_MODE_SHARED_PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val sortingPreference = sharedPrefs.getBoolean(SORT_AZ_ON_KEY, true)

        val sortMode: DogBreedsViewModel.SortMode = if (sortingPreference) {
            DogBreedsViewModel.SortMode.AZ
        } else DogBreedsViewModel.SortMode.ZA

        return sortMode
    }

    private fun setUpRecyclerView() {
        binding.dogBreedsRecyclerView.apply {
            recyclerAdapter = DogBreedsAdapter(
                { dogBreed -> onDogBreedCLick(dogBreed) },
                { onDogBreedLongClick() }
            )
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun onDogBreedCLick(dogBreed: DogBreed) {
        transferDataToDogBreedDetailsFragment(dogBreed)
        (activity as MainActivity).openDogBreedDetails()
    }

    private fun onDogBreedLongClick(): Boolean {
        val specialDog = viewModel.getSpecialDog()
        transferDataToDogBreedDetailsFragment(specialDog)
        (activity as MainActivity).openDogBreedDetails()
        return true
    }

    private fun observeDogBreedStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.dogBreedsStateFlow.collect { response ->

                    recyclerAdapter?.setDogBreedsList(response)
                }
            }
        }
    }

    private fun setUpSearchView() {
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

    private fun onDogOfTheDayButtonClick() {
        binding.dogOfTheDay.setOnClickListener {
            (activity as MainActivity).openDogOfTheDayFragment()
        }
    }

    private fun transferDataToDogBreedDetailsFragment(dogBreed: DogBreed) {
        val bundle = bundleOf(KEY_DOG_BREED to dogBreed)
        setFragmentResult(REQUEST_KEY_DOG_BREED, bundle)
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
        const val REQUEST_KEY_DOG_BREED = "breed_fragment_result_key"
        const val KEY_DOG_BREED = "key_breed"
        fun newInstance() = DogBreedsFragment()
    }
}