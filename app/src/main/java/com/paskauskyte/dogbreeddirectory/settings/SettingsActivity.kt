package com.paskauskyte.dogbreeddirectory.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.paskauskyte.dogbreeddirectory.R
import com.paskauskyte.dogbreeddirectory.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onSortButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.alphabetically ->
                    if (checked) {
                        // Sort alphabetically
                    }

                R.id.alphabeticallyReversed ->
                    if (checked) {
                        // Sort alphabetically reversed
                    }

                R.id.byWeight ->
                    if (checked) {
                        // Sort by weight
                    }

                R.id.byWeightReversed ->
                    if (checked) {
                        // Sort by weight reversed
                    }
            }
        }
    }
}