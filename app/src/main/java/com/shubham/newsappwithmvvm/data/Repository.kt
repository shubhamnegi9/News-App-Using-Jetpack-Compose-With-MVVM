package com.shubham.newsappwithmvvm.data

import com.shubham.newsappwithmvvm.network.NewsManager

class Repository(val newsManager: NewsManager) {
    suspend fun getArticles() = newsManager.getArticles()

    suspend fun getArticlesByCategory(category: String) = newsManager.getArticlesByCategory(category)

    suspend fun getArticlesBySources(sourceName: String) = newsManager.getArticlesBySources(sourceName)

    suspend fun getArticlesByQuery(query: String) = newsManager.getArticlesByQuery(query)
}