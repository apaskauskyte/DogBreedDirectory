package com.paskauskyte.dogbreeddirectory.dog_breed_details

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.size.ViewSizeResolver
import com.paskauskyte.dogbreeddirectory.Constants.SPECIAL_DOG_IMAGE_URL
import com.paskauskyte.dogbreeddirectory.Constants.FAVORITES_SHARED_PREFS_NAME
import com.paskauskyte.dogbreeddirectory.R
import com.paskauskyte.dogbreeddirectory.databinding.FragmentDogBreedDetailsBinding
import com.paskauskyte.dogbreeddirectory.dog_breeds.DogBreedsFragment
import com.paskauskyte.dogbreeddirectory.favorites.FavoritesFragment
import com.paskauskyte.dogbreeddirectory.repository.DogBreed
import com.paskauskyte.dogbreeddirectory.repository.DogBreedRepository
import kotlinx.coroutines.launch

class DogBreedDetailsFragment : Fragment() {

    private val viewModel: DogBreedDetailsViewModel by viewModels {
        DogBreedDetailsViewModelFactory(DogBreedRepository())
    }

    private var _binding: FragmentDogBreedDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDogBreedDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setSharedPreferences(getSharedPref())
        onClickFavoriteButton()
        receiveDataFromDogBreedsFragment()
        receiveDataFromFavoritesFragment()
        observeDogBreedDetails()
        observeFavoriteButtonImage()
    }

    private fun getSharedPref(): SharedPreferences {
        return requireActivity().getSharedPreferences(
            FAVORITES_SHARED_PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    private fun onClickFavoriteButton() {
        binding.favoriteButton.setOnClickListener {
            viewModel.toggleDogSelectionToFavorites()
        }
    }

    private fun receiveDataFromDogBreedsFragment() {
        setFragmentResultListener(DogBreedsFragment.REQUEST_KEY_DOG_BREED) { requestKey, bundle ->
            val breed = bundle.getParcelable<DogBreed>(DogBreedsFragment.KEY_DOG_BREED)
                ?: return@setFragmentResultListener
            viewModel.assignDogBreed(breed)
            viewModel.getFavoriteButtonImageStateFlow()
        }
    }

    private fun receiveDataFromFavoritesFragment() {
        setFragmentResultListener(FavoritesFragment.REQUEST_KEY_FAVORITES) { requestKey, bundle ->
            val breed = bundle.getParcelable<DogBreed>(FavoritesFragment.KEY_FAVORITE_DOG)
                ?: return@setFragmentResultListener
            viewModel.assignDogBreed(breed)
            viewModel.getFavoriteButtonImageStateFlow()
        }
    }

    private fun observeDogBreedDetails() {
        viewModel.breedLiveData.observe(
            viewLifecycleOwner
        ) { breed ->
            binding.apply {
                breedName.text = breed.name

                if (breed.bredFor.isNullOrEmpty()) {
                    binding.bredFor.visibility = View.GONE
                } else {
                    bredFor.text = getString(R.string.bredForText, breed.bred_for)
                }

                if (breed.temperament.isNullOrEmpty()) {
                    binding.temperament.visibility = View.GONE
                } else {
                    temperament.text = getString(R.string.temperamentText, breed.temperament)
                }

                if (breed.origin.isNullOrEmpty()) {
                    binding.origin.visibility = View.GONE
                } else {
                    origin.text = getString(R.string.originText, breed.origin)
                }

                if (breed.lifeSpan.isNullOrEmpty()) {
                    binding.lifeSpan.visibility = View.GONE
                } else {
                    lifeSpan.text = getString(R.string.lifeSpanText, breed.life_span)
                }

                viewModel.dogImageUrlLiveData.observe(
                    viewLifecycleOwner
                ) { imageUrl ->
                    if (breed.imageId == "specialDog") {
                        breedImageView.load(SPECIAL_DOG_IMAGE_URL) {
                            size(ViewSizeResolver(breedImageView))
                        }
                    } else if (imageUrl.isNullOrEmpty()) {
                        breedImageView.visibility = View.GONE
                    } else {
                        breedImageView.load(imageUrl) {
                            size(ViewSizeResolver(breedImageView))
                        }
                    }
                }
            }
        }
    }

    private fun observeFavoriteButtonImage() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.favoriteButtonStateFlow.collect { response ->

                    setFavoriteButtonImage(response)
                }
            }
        }
    }

    private fun setFavoriteButtonImage(dogIsFavorite: Boolean) {
        if (dogIsFavorite) {
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_24)
        }
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
        const val TAG = "dog_breed_details_fragment"
        fun newInstance() = DogBreedDetailsFragment()
    }
}