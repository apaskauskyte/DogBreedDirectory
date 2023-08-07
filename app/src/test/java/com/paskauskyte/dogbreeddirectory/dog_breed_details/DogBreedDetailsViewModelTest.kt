package com.paskauskyte.dogbreeddirectory.dog_breed_details

import android.content.SharedPreferences
import com.google.gson.Gson
import com.paskauskyte.dogbreeddirectory.Constants.FAVORITE_ON_KEY
import com.paskauskyte.dogbreeddirectory.repository.DogBreed
import com.paskauskyte.dogbreeddirectory.repository.Image
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
class DogBreedDetailsViewModelTest {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor

    private lateinit var viewModel: DogBreedDetailsViewModel

    private val dogBreed = DogBreed(
        1, "ADog", "", "", "", "", Image("")
    )

    @BeforeEach
    fun setup() {
        viewModel = DogBreedDetailsViewModel()
        sharedPreferences = mockk()
        editor = mockk(relaxed = true)
        every { sharedPreferences.edit() } returns editor
        viewModel.setSharedPreferences(sharedPreferences)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `assignDogBreed updates breed live data`() {
        // Act
        viewModel.assignDogBreed(dogBreed)

        // Assert
        assertEquals(dogBreed, viewModel.breedLiveData.value)
    }

    @Test
    fun `selecting dog as favorite adds the dog to favorites when it is not in the list`() {
        // Arrange
        val emptyFavoritesList = emptyList<DogBreed>()
        val newListOfFavDogs = mutableListOf(dogBreed)
        val newListOfFavDogsAsString = Gson().toJson(newListOfFavDogs)

        every { sharedPreferences.getString(any(), any()) } returns emptyFavoritesList.toString()
        viewModel.assignDogBreed(dogBreed)

        // Act
        viewModel.toggleDogSelectionToFavorites()

        // Assert
        verify { editor.putString(FAVORITE_ON_KEY, newListOfFavDogsAsString) }
    }

    @Test
    fun `unselecting dog from favorites removes the dog from favorites when it is in the list`() {
        // Arrange
        val favoritesList = mutableListOf(dogBreed)
        val newListOfFavDogsAsString = Gson().toJson(favoritesList)

        every { sharedPreferences.getString(any(), any()) } returns newListOfFavDogsAsString
        viewModel.assignDogBreed(dogBreed)

        // Act
        viewModel.toggleDogSelectionToFavorites()

        // Assert
        verify { editor.putString(FAVORITE_ON_KEY, "[]") }
    }

    @Test
    fun `adding dog to favorites changes the value of the favorites button state flow to true`() {
        // Arrange
        val emptyFavoritesList = emptyList<DogBreed>()

        every { sharedPreferences.getString(any(), any()) } returns emptyFavoritesList.toString()
        viewModel.assignDogBreed(dogBreed)

        // Act
        viewModel.toggleDogSelectionToFavorites()

        // Assert
        assertEquals(true, viewModel.favoriteButtonStateFlow.value)
    }

    @Test
    fun `removing dog from favorites changes the value of the favorites button state flow to false`() {
        // Arrange
        val favoritesList = mutableListOf(dogBreed)
        val newListOfFavDogsAsString = Gson().toJson(favoritesList)

        every { sharedPreferences.getString(any(), any()) } returns newListOfFavDogsAsString
        viewModel.assignDogBreed(dogBreed)

        // Act
        viewModel.toggleDogSelectionToFavorites()

        // Assert
        assertEquals(false, viewModel.favoriteButtonStateFlow.value)
    }

    @Test
    fun `getFavoriteButtonImageStateFlow sets value to false when breed is not in favorites list`() {
        // Arrange
        val emptyFavoritesList = emptyList<DogBreed>()

        every { sharedPreferences.getString(any(), any()) } returns emptyFavoritesList.toString()
        viewModel.assignDogBreed(dogBreed)

        // Act
        viewModel.getFavoriteButtonImageStateFlow()

        // Assert
        assertEquals(false, viewModel.favoriteButtonStateFlow.value)
    }

    @Test
    fun `getFavoriteButtonImageStateFlow sets value to true when breed is in favorites list`() {
        // Arrange
        val favoritesList = mutableListOf(dogBreed)

        every { sharedPreferences.getString(any(), any()) } returns Gson().toJson(favoritesList)
        viewModel.assignDogBreed(dogBreed)

        // Act
        viewModel.getFavoriteButtonImageStateFlow()

        // Assert
        assertEquals(true, viewModel.favoriteButtonStateFlow.value)
    }
}