package com.paskauskyte.dogbreeddirectory.settings

import android.content.Context
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

        readSortSettingButton()
    }

    fun onSortButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.az ->
                    if (checked) {
                        saveSortSettingButton(true)
                    }

                R.id.za ->
                    if (checked) {
                        saveSortSettingButton(false)
                    }
            }
        }
    }

    private fun saveSortSettingButton(value: Boolean) {
        val sharedPreferences = this.getSharedPreferences("sort_preference", Context.MODE_PRIVATE) ?: return
        with(sharedPreferences.edit()) {
            putBoolean("key_sort_az_on", value)
            apply()
        }
    }

    private fun readSortSettingButton() {
        val sharedPreferences = this.getSharedPreferences("sort_preference", Context.MODE_PRIVATE) ?: return
        val defaultValue = true
        val sortAlphabeticallyIsOn = sharedPreferences.getBoolean(
            "key_sort_az_on",
            defaultValue
        )

        if (sortAlphabeticallyIsOn) {
            binding.az.isChecked = true
            binding.za.isChecked = false
        } else {
            binding.az.isChecked = false
            binding.za.isChecked = true
        }
    }
}