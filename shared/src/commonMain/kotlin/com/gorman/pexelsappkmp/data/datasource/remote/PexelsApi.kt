package com.gorman.pexelsappkmp.data.datasource.remote

import com.gorman.pexelsappkmp.data.models.CollectionsResponse
import com.gorman.pexelsappkmp.data.models.PexelsResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface PexelsApi {
    @GET("curated")
    suspend fun searchCuratedPhotos(
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): PexelsResponse

    @GET("search")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): PexelsResponse

    @GET("collections/featured")
    suspend fun searchFeaturedCollections(
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1
    ): CollectionsResponse
}