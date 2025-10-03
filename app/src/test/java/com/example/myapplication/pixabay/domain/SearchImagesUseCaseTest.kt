package com.example.myapplication.pixabay.domain

import com.example.myapplication.pixabay.data.PixabayRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchImagesUseCaseTest {

    private lateinit var repository: PixabayRepository
    private lateinit var useCase: SearchImagesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = SearchImagesUseCase(repository)
    }

    @Test
    fun `invoke should call repository search with correct parameters`() = runTest {
        // Given
        val query = "nature"
        val page = 1
        val perPage = 25
        
        val expectedResult = PixabaySearchPage(
            total = 100,
            totalHits = 50,
            images = listOf(
                PixabayImage(
                    id = 123L,
                    tags = listOf("nature", "landscape"),
                    previewUrl = "https://example.com/preview.jpg",
                    webformatUrl = "https://example.com/webformat.jpg",
                    largeImageUrl = "https://example.com/large.jpg",
                    userName = "photographer"
                )
            )
        )

        coEvery { 
            repository.search(query = query, page = page, perPage = perPage) 
        } returns expectedResult

        // When
        val result = useCase(query, page, perPage)

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should use default perPage value when not specified`() = runTest {
        // Given
        val query = "mountain"
        val page = 2
        val expectedPerPage = 25 // Default value
        
        val expectedResult = PixabaySearchPage(
            total = 200,
            totalHits = 100,
            images = emptyList()
        )

        coEvery { 
            repository.search(query = query, page = page, perPage = expectedPerPage) 
        } returns expectedResult

        // When
        val result = useCase(query, page) // Not specifying perPage

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `invoke should pass through custom perPage value`() = runTest {
        // Given
        val query = "ocean"
        val page = 3
        val customPerPage = 50
        
        val expectedResult = PixabaySearchPage(
            total = 300,
            totalHits = 150,
            images = listOf(
                PixabayImage(
                    id = 456L,
                    tags = listOf("ocean", "water", "blue"),
                    previewUrl = "https://example.com/ocean_preview.jpg",
                    webformatUrl = "https://example.com/ocean_webformat.jpg",
                    largeImageUrl = "https://example.com/ocean_large.jpg",
                    userName = "oceanlover"
                ),
                PixabayImage(
                    id = 789L,
                    tags = listOf("sea", "waves", "sunset"),
                    previewUrl = "https://example.com/sea_preview.jpg",
                    webformatUrl = "https://example.com/sea_webformat.jpg",
                    largeImageUrl = "https://example.com/sea_large.jpg",
                    userName = "sealover"
                )
            )
        )

        coEvery { 
            repository.search(query = query, page = page, perPage = customPerPage) 
        } returns expectedResult

        // When
        val result = useCase(query, page, customPerPage)

        // Then
        assertEquals(expectedResult, result)
        assertEquals(2, result.images.size)
        assertEquals(456L, result.images[0].id)
        assertEquals(789L, result.images[1].id)
    }

    @Test
    fun `invoke should handle empty search results`() = runTest {
        // Given
        val query = "nonexistent"
        val page = 1
        val perPage = 25
        
        val expectedResult = PixabaySearchPage(
            total = 0,
            totalHits = 0,
            images = emptyList()
        )

        coEvery { 
            repository.search(query = query, page = page, perPage = perPage) 
        } returns expectedResult

        // When
        val result = useCase(query, page, perPage)

        // Then
        assertEquals(expectedResult, result)
        assert(result.images.isEmpty())
        assertEquals(0, result.total)
        assertEquals(0, result.totalHits)
    }

    @Test
    fun `invoke should handle large page numbers`() = runTest {
        // Given
        val query = "city"
        val page = 100
        val perPage = 20
        
        val expectedResult = PixabaySearchPage(
            total = 2000,
            totalHits = 2000,
            images = listOf(
                PixabayImage(
                    id = 999L,
                    tags = listOf("city", "urban", "skyline"),
                    previewUrl = "https://example.com/city_preview.jpg",
                    webformatUrl = "https://example.com/city_webformat.jpg",
                    largeImageUrl = "https://example.com/city_large.jpg",
                    userName = "cityphotographer"
                )
            )
        )

        coEvery { 
            repository.search(query = query, page = page, perPage = perPage) 
        } returns expectedResult

        // When
        val result = useCase(query, page, perPage)

        // Then
        assertEquals(expectedResult, result)
        assertEquals(1, result.images.size)
        assertEquals(999L, result.images[0].id)
    }

    @Test
    fun `invoke should handle special characters in query`() = runTest {
        // Given
        val query = "sunset & sunrise"
        val page = 1
        val perPage = 25
        
        val expectedResult = PixabaySearchPage(
            total = 50,
            totalHits = 25,
            images = listOf(
                PixabayImage(
                    id = 555L,
                    tags = listOf("sunset", "sunrise", "golden hour"),
                    previewUrl = "https://example.com/sunset_preview.jpg",
                    webformatUrl = "https://example.com/sunset_webformat.jpg",
                    largeImageUrl = "https://example.com/sunset_large.jpg",
                    userName = "goldenhour"
                )
            )
        )

        coEvery { 
            repository.search(query = query, page = page, perPage = perPage) 
        } returns expectedResult

        // When
        val result = useCase(query, page, perPage)

        // Then
        assertEquals(expectedResult, result)
        assertEquals(1, result.images.size)
        assertEquals(555L, result.images[0].id)
        assertEquals("goldenhour", result.images[0].userName)
    }
}


