package com.paskauskyte.dogbreeddirectory.repository

import com.paskauskyte.dogbreeddirectory.dog_api.ApiService
import com.paskauskyte.dogbreeddirectory.dog_api.DogApiServiceClient
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class DogBreedRepositoryTest {

    private lateinit var repository: DogBreedRepository

    private val apiService = mockk<ApiService>()

    private val dog1 = DogBreed(
        1, "ADog", "", "", "", "", Image("")
    )
    private val dog2 = DogBreed(
        2, "BDog", "", "", "", "", Image("")
    )
    private val dog3 = DogBreed(
        3, "CDog", "", "", "", "", Image("")
    )

    private val dogList = listOf(dog1, dog2, dog3)

    @BeforeEach
    fun setUp() {
        repository = DogBreedRepository()
        mockkObject(DogApiServiceClient)
        coEvery { DogApiServiceClient.providesApiService() } returns apiService
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `fetchDogList returns dog list when API call is successful`() = runTest {
        // Arrange
        coEvery { apiService.getDogBreeds() } returns Response.success(dogList)

        // Act
        val result = repository.fetchDogList()

        // Assert
        assertEquals(dogList, result)
    }

    @Test
    fun `fetchDogList returns empty dog list when API call fails`() = runTest {
        // Arrange (unsuccessful ApiService response)
        coEvery { apiService.getDogBreeds() } returns Response.error(
            404,
            "{}".toResponseBody(null)
        )

        // Act
        val result = repository.fetchDogList()

        // Assert
        assertEquals(emptyList<DogBreed>(), result)
    }
}