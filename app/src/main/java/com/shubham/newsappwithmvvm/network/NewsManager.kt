package com.shubham.newsappwithmvvm.network

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.shubham.newsappwithmvvm.data.models.ArticleCategory
import com.shubham.newsappwithmvvm.data.models.TopNewsResponse
import com.shubham.newsappwithmvvm.data.models.getArticleCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsManager(val newsService: NewsService) {

    suspend fun getArticles(): TopNewsResponse
    = withContext(Dispatchers.IO){
        newsService.getTopNewsArticles(ApiUtils.COUNTRY_US, ApiUtils.API_KEY)
    }

    suspend fun getArticlesByCategory(category: String): TopNewsResponse
    = withContext(Dispatchers.IO){
        newsService.getTopNewsArticlesByCategory(category, ApiUtils.API_KEY)
    }

    suspend fun getArticlesBySources(sourceName: String): TopNewsResponse
    = withContext(Dispatchers.IO){
       newsService.getTopNewsArticlesBySources(sourceName, ApiUtils.API_KEY)
    }

    suspend fun getArticlesByQuery(query: String): TopNewsResponse
    = withContext(Dispatchers.IO){
        newsService.getTopNewsArticlesByQuery(query, ApiUtils.API_KEY)
    }
}