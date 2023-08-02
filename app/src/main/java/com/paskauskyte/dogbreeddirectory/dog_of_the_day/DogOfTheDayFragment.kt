package com.paskauskyte.dogbreeddirectory.dog_of_the_day

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.size.ViewSizeResolver
import com.paskauskyte.dogbreeddirectory.R
import com.paskauskyte.dogbreeddirectory.databinding.FragmentDogOfTheDayBinding

class DogOfTheDayFragment : Fragment() {

    private val viewModel: DogOfTheDayViewModel by viewModels()

    private var _binding: FragmentDogOfTheDayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDogOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDogOfTheDay()
    }

    private fun observeDogOfTheDay() {
        viewModel.dogOfTheDayLiveData.observe(
            viewLifecycleOwner
        ) { dogOfTheDay ->
            binding.apply {
                breedName.text = dogOfTheDay.name

                if (dogOfTheDay.bred_for.isNullOrEmpty()) {
                    binding.bredFor.visibility = View.GONE
                } else {
                    bredFor.text = getString(R.string.bredForText, dogOfTheDay.bred_for)
                }

                if (dogOfTheDay.temperament.isNullOrEmpty()) {
                    binding.temperament.visibility = View.GONE
                } else {
                    temperament.text = getString(R.string.temperamentText, dogOfTheDay.temperament)
                }

                if (dogOfTheDay.origin.isNullOrEmpty()) {
                    binding.origin.visibility = View.GONE
                } else {
                    origin.text = getString(R.string.originText, dogOfTheDay.origin)
                }

                if (dogOfTheDay.life_span.isNullOrEmpty()) {
                    binding.lifeSpan.visibility = View.GONE
                } else {
                    lifeSpan.text = getString(R.string.lifeSpanText, dogOfTheDay.life_span)
                }

                val url = dogOfTheDay.image.url
                if (url.isNullOrEmpty()) {
                    binding.breedImageView.visibility = View.GONE
                } else {
                    breedImageView.load(url) {
                        size(ViewSizeResolver(breedImageView))
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "dog_of_the_day_fragment"
        fun newInstance() = DogOfTheDayFragment()
    }
}