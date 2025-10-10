package com.example.myapplication.pixabay.data

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PixabayRepositoryImplTest {

    private lateinit var pixabayApi: PixabayApi
    private lateinit var repository: PixabaySearchRepositoryImpl

    @Before
    fun setup() {
        pixabayApi = mockk()
        repository = PixabaySearchRepositoryImpl(pixabayApi)
    }

    @Test
    fun `search should return mapped PixabaySearchPage when API call succeeds`() = runTest {
        // Given
        val query = "nature"
        val page = 1
        val perPage = 25
        
        val mockResponse = PixabaySearchResponse(
            total = 100,
            totalHits = 50,
            hits = listOf(
                PixabayHitDto(
                    id = 123L,
                    pageURL = "https://pixabay.com/photos/nature-123/",
                    type = "photo",
                    tags = "nature, landscape, forest",
                    previewURL = "https://cdn.pixabay.com/photo/2023/01/01/nature-123_150.jpg",
                    previewWidth = 150,
                    previewHeight = 100,
                    webformatURL = "https://cdn.pixabay.com/photo/2023/01/01/nature-123_640.jpg",
                    webformatWidth = 640,
                    webformatHeight = 427,
                    largeImageURL = "https://cdn.pixabay.com/photo/2023/01/01/nature-123_1280.jpg",
                    imageWidth = 1280,
                    imageHeight = 853,
                    imageSize = 500000,
                    views = 1000,
                    downloads = 50,
                    collections = 10,
                    likes = 25,
                    comments = 5,
                    userId = 456,
                    user = "photographer123",
                    userImageURL = "https://cdn.pixabay.com/user/2023/01/01/photographer123_50.jpg"
                ),
                PixabayHitDto(
                    id = 456L,
                    pageURL = "https://pixabay.com/photos/mountain-456/",
                    type = "photo",
                    tags = "mountain, peak, snow",
                    previewURL = "https://cdn.pixabay.com/photo/2023/01/02/mountain-456_150.jpg",
                    previewWidth = 150,
                    previewHeight = 100,
                    webformatURL = "https://cdn.pixabay.com/photo/2023/01/02/mountain-456_640.jpg",
                    webformatWidth = 640,
                    webformatHeight = 427,
                    largeImageURL = "https://cdn.pixabay.com/photo/2023/01/02/mountain-456_1280.jpg",
                    imageWidth = 1280,
                    imageHeight = 853,
                    imageSize = 600000,
                    views = 2000,
                    downloads = 100,
                    collections = 20,
                    likes = 50,
                    comments = 10,
                    userId = 789,
                    user = "mountainlover",
                    userImageURL = "https://cdn.pixabay.com/user/2023/01/02/mountainlover_50.jpg"
                )
            )
        )

        coEvery { 
            pixabayApi.searchImages(query = query, page = page, perPage = perPage) 
        } returns mockResponse

        // When
        val result = repository.search(query, page, perPage)

        // Then
        assertEquals(100, result.total)
        assertEquals(50, result.totalHits)
        assertEquals(2, result.images.size)

        val firstImage = result.images[0]
        assertEquals(123L, firstImage.id)
        assertEquals(listOf("nature", "landscape", "forest"), firstImage.tags)
        assertEquals("https://cdn.pixabay.com/photo/2023/01/01/nature-123_150.jpg", firstImage.previewUrl)
        assertEquals("https://cdn.pixabay.com/photo/2023/01/01/nature-123_640.jpg", firstImage.webformatUrl)
        assertEquals("https://cdn.pixabay.com/photo/2023/01/01/nature-123_1280.jpg", firstImage.largeImageUrl)
        assertEquals("photographer123", firstImage.userName)

        val secondImage = result.images[1]
        assertEquals(456L, secondImage.id)
        assertEquals(listOf("mountain", "peak", "snow"), secondImage.tags)
        assertEquals("https://cdn.pixabay.com/photo/2023/01/02/mountain-456_150.jpg", secondImage.previewUrl)
        assertEquals("https://cdn.pixabay.com/photo/2023/01/02/mountain-456_640.jpg", secondImage.webformatUrl)
        assertEquals("https://cdn.pixabay.com/photo/2023/01/02/mountain-456_1280.jpg", secondImage.largeImageUrl)
        assertEquals("mountainlover", secondImage.userName)
    }

    @Test
    fun `search should handle empty tags string correctly`() = runTest {
        // Given
        val query = "test"
        val page = 1
        val perPage = 25
        
        val mockResponse = PixabaySearchResponse(
            total = 1,
            totalHits = 1,
            hits = listOf(
                PixabayHitDto(
                    id = 789L,
                    pageURL = "https://pixabay.com/photos/test-789/",
                    type = "photo",
                    tags = "", // Empty tags
                    previewURL = "https://cdn.pixabay.com/photo/2023/01/03/test-789_150.jpg",
                    previewWidth = 150,
                    previewHeight = 100,
                    webformatURL = "https://cdn.pixabay.com/photo/2023/01/03/test-789_640.jpg",
                    webformatWidth = 640,
                    webformatHeight = 427,
                    largeImageURL = "https://cdn.pixabay.com/photo/2023/01/03/test-789_1280.jpg",
                    imageWidth = 1280,
                    imageHeight = 853,
                    imageSize = 400000,
                    views = 500,
                    downloads = 25,
                    collections = 5,
                    likes = 15,
                    comments = 3,
                    userId = 101,
                    user = "testuser",
                    userImageURL = "https://cdn.pixabay.com/user/2023/01/03/testuser_50.jpg"
                )
            )
        )

        coEvery { 
            pixabayApi.searchImages(query = query, page = page, perPage = perPage) 
        } returns mockResponse

        // When
        val result = repository.search(query, page, perPage)

        // Then
        assertEquals(1, result.images.size)
        val image = result.images[0]
        assertEquals(789L, image.id)
        assertTrue(image.tags.isEmpty())
        assertEquals("testuser", image.userName)
    }

    @Test
    fun `search should handle null tags correctly`() = runTest {
        // Given
        val query = "test"
        val page = 1
        val perPage = 25
        
        val mockResponse = PixabaySearchResponse(
            total = 1,
            totalHits = 1,
            hits = listOf(
                PixabayHitDto(
                    id = 999L,
                    pageURL = "https://pixabay.com/photos/test-999/",
                    type = "photo",
                    tags = null, // Null tags
                    previewURL = "https://cdn.pixabay.com/photo/2023/01/04/test-999_150.jpg",
                    previewWidth = 150,
                    previewHeight = 100,
                    webformatURL = "https://cdn.pixabay.com/photo/2023/01/04/test-999_640.jpg",
                    webformatWidth = 640,
                    webformatHeight = 427,
                    largeImageURL = "https://cdn.pixabay.com/photo/2023/01/04/test-999_1280.jpg",
                    imageWidth = 1280,
                    imageHeight = 853,
                    imageSize = 300000,
                    views = 300,
                    downloads = 15,
                    collections = 3,
                    likes = 8,
                    comments = 2,
                    userId = 202,
                    user = "nulltagsuser",
                    userImageURL = "https://cdn.pixabay.com/user/2023/01/04/nulltagsuser_50.jpg"
                )
            )
        )

        coEvery { 
            pixabayApi.searchImages(query = query, page = page, perPage = perPage) 
        } returns mockResponse

        // When
        val result = repository.search(query, page, perPage)

        // Then
        assertEquals(1, result.images.size)
        val image = result.images[0]
        assertEquals(999L, image.id)
        assertTrue(image.tags.isEmpty())
        assertEquals("nulltagsuser", image.userName)
    }

    @Test
    fun `search should handle tags with extra spaces correctly`() = runTest {
        // Given
        val query = "test"
        val page = 1
        val perPage = 25
        
        val mockResponse = PixabaySearchResponse(
            total = 1,
            totalHits = 1,
            hits = listOf(
                PixabayHitDto(
                    id = 111L,
                    pageURL = "https://pixabay.com/photos/test-111/",
                    type = "photo",
                    tags = "  nature  ,  landscape  ,  ,  forest  ,  ", // Tags with extra spaces and empty elements
                    previewURL = "https://cdn.pixabay.com/photo/2023/01/05/test-111_150.jpg",
                    previewWidth = 150,
                    previewHeight = 100,
                    webformatURL = "https://cdn.pixabay.com/photo/2023/01/05/test-111_640.jpg",
                    webformatWidth = 640,
                    webformatHeight = 427,
                    largeImageURL = "https://cdn.pixabay.com/photo/2023/01/05/test-111_1280.jpg",
                    imageWidth = 1280,
                    imageHeight = 853,
                    imageSize = 350000,
                    views = 400,
                    downloads = 20,
                    collections = 4,
                    likes = 12,
                    comments = 1,
                    userId = 303,
                    user = "spacestagsuser",
                    userImageURL = "https://cdn.pixabay.com/user/2023/01/05/spacestagsuser_50.jpg"
                )
            )
        )

        coEvery { 
            pixabayApi.searchImages(query = query, page = page, perPage = perPage) 
        } returns mockResponse

        // When
        val result = repository.search(query, page, perPage)

        // Then
        assertEquals(1, result.images.size)
        val image = result.images[0]
        assertEquals(111L, image.id)
        assertEquals(listOf("nature", "landscape", "forest"), image.tags)
        assertEquals("spacestagsuser", image.userName)
    }

    @Test
    fun `search should use default perPage value when not specified`() = runTest {
        // Given
        val query = "test"
        val page = 1
        val expectedPerPage = 25 // Default value
        
        val mockResponse = PixabaySearchResponse(
            total = 0,
            totalHits = 0,
            hits = emptyList()
        )

        coEvery { 
            pixabayApi.searchImages(query = query, page = page, perPage = expectedPerPage) 
        } returns mockResponse

        // When
        repository.search(query, page) // Not specifying perPage

        // Then
        // Verify that the API was called with the default perPage value
        coEvery { 
            pixabayApi.searchImages(query = query, page = page, perPage = expectedPerPage) 
        } returns mockResponse
    }
}


