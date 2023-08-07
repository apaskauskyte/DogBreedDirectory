package com.paskauskyte.dogbreeddirectory.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.paskauskyte.dogbreeddirectory.Constants.FAVORITES_SHARED_PREFS_NAME
import com.paskauskyte.dogbreeddirectory.Constants.FAVORITE_ON_KEY
import com.paskauskyte.dogbreeddirectory.MainActivity
import com.paskauskyte.dogbreeddirectory.databinding.FragmentFavoritesBinding
import com.paskauskyte.dogbreeddirectory.dog_breed_details.DogBreedDetailsViewModel
import com.paskauskyte.dogbreeddirectory.favorites.recyclerview.FavoritesAdapter
import com.paskauskyte.dogbreeddirectory.repository.DogBreed

class FavoritesFragment : Fragment() {

    private val viewModel: DogBreedDetailsViewModel by viewModels()

    private var recyclerAdapter: FavoritesAdapter? = null

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        getFavoriteDogsList()
        observeDogBreed()
    }

    private fun setUpRecyclerView() {
        binding.favoritesRecyclerView.apply {
            recyclerAdapter = FavoritesAdapter { dogBreed -> onDogBreedClick(dogBreed) }
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun getFavoriteDogsList() {
        val sharedPref = activity?.getSharedPreferences(
            FAVORITES_SHARED_PREFS_NAME,
            Context.MODE_PRIVATE
        ) ?: return

        val favoriteDogsListAsString =
            sharedPref.getString(FAVORITE_ON_KEY, emptyList<DogBreed>().toString())

        val listType = object : TypeToken<List<DogBreed>>() {}.type

        val favoriteDogsList =
            Gson().fromJson<List<DogBreed>>(favoriteDogsListAsString, listType).toMutableList()

        submitFavDogs(favoriteDogsList)

        val text: TextView = binding.textIfEmpty

        if (favoriteDogsList.isEmpty()) {
            text.visibility = View.VISIBLE
        } else {
            text.visibility = View.GONE
        }
    }

    private fun submitFavDogs(list: List<DogBreed>) {
        recyclerAdapter?.submitList(list)
        binding.favoritesRecyclerView.adapter = recyclerAdapter
    }

    private fun observeDogBreed() {
        viewModel.breedLiveData.observe(viewLifecycleOwner) { dogBreed ->
            viewModel.assignDogBreed(dogBreed)
        }
    }

    private fun onDogBreedClick(dogBreed: DogBreed) {
        transferDataToDogBreedDetailsFragment(dogBreed)
        (activity as MainActivity).openDogBreedDetails()
    }

    private fun transferDataToDogBreedDetailsFragment(dogBreed: DogBreed) {
        val bundle = bundleOf(KEY_FAVORITE_DOG to dogBreed)
        setFragmentResult(REQUEST_KEY_FAVORITES, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "favorites_fragment"
        const val REQUEST_KEY_FAVORITES = "favorites_fragment_result_key"
        const val KEY_FAVORITE_DOG = "key_favorite_dog"
        fun newInstance() = FavoritesFragment()
    }
}