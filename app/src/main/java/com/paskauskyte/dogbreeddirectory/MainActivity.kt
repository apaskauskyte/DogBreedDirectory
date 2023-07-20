package com.paskauskyte.dogbreeddirectory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.paskauskyte.dogbreeddirectory.databinding.ActivityMainBinding
import com.paskauskyte.dogbreeddirectory.dog_breeds.DogBreedsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            openDogBreedsFragment()
        }
    }

    private fun openDogBreedsFragment() {
        setCurrentFragment(DogBreedsFragment.newInstance(), DogBreedsFragment.TAG)
    }

    private fun setCurrentFragment(fragment: Fragment, tag: String, addBackStack: Boolean = false) {
        supportFragmentManager.commit {
            replace(
                R.id.fragment_container_view,
                fragment,
                tag
            )

            setReorderingAllowed(true)

            if (addBackStack) {
                addToBackStack(tag)
            }
        }
    }
}