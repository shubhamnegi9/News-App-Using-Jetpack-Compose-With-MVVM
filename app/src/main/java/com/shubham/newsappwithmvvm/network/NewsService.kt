package com.shubham.newsappwithmvvm.network

import com.shubham.newsappwithmvvm.data.models.TopNewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("top-headlines")
    suspend fun getTopNewsArticles(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): TopNewsResponse

    @GET("top-headlines")
    suspend fun getTopNewsArticlesByCategory(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): TopNewsResponse

    @GET("everything")
    suspend fun getTopNewsArticlesBySources(
        @Query("sources") sources: String,
        @Query("apiKey") apiKey: String
    ): TopNewsResponse

    @GET("everything")
    suspend fun getTopNewsArticlesByQuery(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): TopNewsResponse
}