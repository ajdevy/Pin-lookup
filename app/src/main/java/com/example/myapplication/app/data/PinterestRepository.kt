package com.example.myapplication.app.data

interface PinterestRepository {
    suspend fun getPinsPage(pageSize: Int, bookmark: String? = null): PinsResponse
    suspend fun getPins(params: PagingParams): PinsResponse
    suspend fun getNextPins(currentBookmark: String?, pageSize: Int = 25): PinsResponse
}

class PinterestRepositoryImpl(
    private val api: PinterestApi
) : PinterestRepository {
    override suspend fun getPinsPage(pageSize: Int, bookmark: String?): PinsResponse {
        return api.getPins(pageSize = pageSize, bookmark = bookmark)
    }

    override suspend fun getPins(params: PagingParams): PinsResponse {
        return api.getPins(pageSize = params.pageSize, bookmark = params.bookmark)
    }

    override suspend fun getNextPins(currentBookmark: String?, pageSize: Int): PinsResponse {
        return api.getPins(pageSize = pageSize, bookmark = currentBookmark)
    }
}


