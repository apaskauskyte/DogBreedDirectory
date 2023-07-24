package com.paskauskyte.dogbreeddirectory.dog_breed_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import coil.load
import coil.size.ViewSizeResolver
import com.paskauskyte.dogbreeddirectory.R
import com.paskauskyte.dogbreeddirectory.databinding.FragmentDogBreedDetailsBinding
import com.paskauskyte.dogbreeddirectory.dog_breeds.DogBreed
import com.paskauskyte.dogbreeddirectory.dog_breeds.DogBreedsFragment

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDogBreedDetails()
        receiveDataFromDogBreedsFragment()
    }

    private fun observeDogBreedDetails() {
        viewModel.breedLiveData.observe(
            viewLifecycleOwner
        ) { breed ->
            binding.apply {
                breedName.text = breed.name

                if (breed.bred_for == null) {
                    binding.bredFor.visibility = View.GONE
                } else {
                    bredFor.text = getString(R.string.bredForText, breed.bred_for)
                }

                if (breed.temperament == null) {
                    binding.temperament.visibility = View.GONE
                } else {
                    temperament.text = getString(R.string.temperamentText, breed.temperament)
                }

                if (breed.origin == null) {
                    binding.origin.visibility = View.GONE
                } else {
                    origin.text = getString(R.string.originText, breed.origin)
                }

                if (breed.life_span == null) {
                    binding.lifeSpan.visibility = View.GONE
                } else {
                    lifeSpan.text = getString(R.string.lifeSpanText, breed.life_span)
                }

                val url = breed.image.url
                if (url == null) {
                    binding.breedImageView.visibility = View.GONE
                } else {
                    breedImageView.load(url) {
                        size(ViewSizeResolver(breedImageView))
                    }
                }
            }
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

    private fun receiveDataFromDogBreedsFragment() {
        setFragmentResultListener(DogBreedsFragment.REQUEST_KEY_DOG_BREED) { requestKey, bundle ->
            val breed = bundle.getParcelable<DogBreed>(DogBreedsFragment.KEY_DOG_BREED)
                ?: return@setFragmentResultListener
            viewModel.saveDogBreed(breed)
        }
    }

    companion object {
        const val TAG = "dog_breed_details_fragment"
        fun newInstance() = DogBreedDetailsFragment()
    }
}