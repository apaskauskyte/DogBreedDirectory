package com.paskauskyte.dogbreeddirectory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.paskauskyte.dogbreeddirectory.databinding.ActivityMainBinding
import com.paskauskyte.dogbreeddirectory.dog_breed_details.DogBreedDetailsFragment
import com.paskauskyte.dogbreeddirectory.dog_breeds.DogBreedsFragment
import com.paskauskyte.dogbreeddirectory.settings.SettingsActivity

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

    fun openDogBreedDetails() {
        setCurrentFragment(DogBreedDetailsFragment.newInstance(), DogBreedDetailsFragment.TAG, true)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}