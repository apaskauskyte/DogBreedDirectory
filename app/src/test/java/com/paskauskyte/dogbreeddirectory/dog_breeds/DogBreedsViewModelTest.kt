package com.paskauskyte.dogbreeddirectory.dog_breeds

import com.paskauskyte.dogbreeddirectory.repository.DogBreed
import com.paskauskyte.dogbreeddirectory.repository.DogBreedRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DogBreedsViewModelTest {

    private lateinit var viewModel: DogBreedsViewModel

    private lateinit var mockRepository: DogBreedRepository

    private val dog1 = DogBreed(
        1, "ADog", "", "", "", "", ""
    )
    private val dog2 = DogBreed(
        2, "BDog", "", "", "", "", ""
    )
    private val dog3 = DogBreed(
        3, "CDog", "", "", "", "", ""
    )

    private val dogList = listOf(dog3, dog1, dog2)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @ExperimentalCoroutinesApi
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
        coEvery { mockRepository.fetchDogList() } returns dogList
        viewModel = DogBreedsViewModel(mockRepository, testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `setting different sort mode changes the current sort mode`() {
        // Arrange
        val newSortMode = DogBreedsViewModel.SortMode.ZA
        viewModel.setSortingMode(DogBreedsViewModel.SortMode.AZ)
        val initialSortMode = viewModel.getCurrentSortingMode()

        // Act
        viewModel.setSortingMode(newSortMode)

        // Assert
        assertNotEquals(viewModel.getCurrentSortingMode(), initialSortMode)
    }

    @Test
    fun `setting the same sort mode doesn't change the current sort mode`() {
        // Arrange
        val initialSortMode = DogBreedsViewModel.SortMode.AZ
        viewModel.setSortingMode(initialSortMode)

        // Act
        viewModel.setSortingMode(initialSortMode)

        // Assert
        assertEquals(initialSortMode, viewModel.getCurrentSortingMode())
    }

    @Test
    fun `setSortingMode sorts dog breeds A to Z by name when sort mode is changed to AZ`() {
        // Arrange
        val sortedDogListAZ = listOf(dog1, dog2, dog3)

        // Act
        viewModel.setSortingMode(DogBreedsViewModel.SortMode.AZ)

        // Get the current dog breeds after sorting mode change
        val dogBreedsAfterAZ = viewModel.dogBreedsStateFlow.value

        // Assert
        assertEquals(sortedDogListAZ, dogBreedsAfterAZ)
    }

    @Test
    fun `setSortingMode sorts dog breeds Z to A by name when sort mode is changed to ZA`() {
        // Arrange
        val sortedDogListZA = listOf(dog3, dog2, dog1)

        // Act
        viewModel.setSortingMode(DogBreedsViewModel.SortMode.ZA)

        // Get the current dog breeds after sorting mode change
        val dogBreedsAfterZA = viewModel.dogBreedsStateFlow.value

        // Assert
        assertEquals(sortedDogListZA, dogBreedsAfterZA)
    }

    @Test
    fun `when view model is created getDogList fetches and sorts dog breeds from A to Z by name`() {
        // Arrange
        val sortedDogList = listOf(dog1, dog2, dog3)

        // Assert
        val actualDogList = viewModel.dogBreedsStateFlow.value
        assertEquals(sortedDogList, actualDogList)
    }

    @Test
    fun `empty search field returns full list of dogs`() {
        // Arrange
        val enteredText = ""

        // Act
        viewModel.filterDogBreedList(enteredText)

        // Assert
        val actualDogList = viewModel.dogBreedsStateFlow.value
        assertEquals(dogList, actualDogList)
    }

    @Test
    fun `text entered in search field filters list of dogs based on entered text`() {
        // Arrange
        val enteredText = "a"
        val filteredDogList = listOf(dog1)

        // Act
        viewModel.filterDogBreedList(enteredText)

        // Assert
        val actualDogList = viewModel.dogBreedsStateFlow.value
        assertEquals(filteredDogList, actualDogList)
    }

    @Test
    fun `multiple text changes in search field filters list of dogs`() {
        // Arrange
        val enteredText1 = "a"
        val enteredText2 = "b"
        val filteredDogList1 = listOf(dog1)
        val filteredDogList2 = listOf(dog2)

        // Act
        viewModel.filterDogBreedList(enteredText1)
        val actualDogList1 = viewModel.dogBreedsStateFlow.value

        viewModel.filterDogBreedList(enteredText2)
        val actualDogList2 = viewModel.dogBreedsStateFlow.value

        // Assert
        assertEquals(filteredDogList1, actualDogList1)
        assertEquals(filteredDogList2, actualDogList2)
    }
}