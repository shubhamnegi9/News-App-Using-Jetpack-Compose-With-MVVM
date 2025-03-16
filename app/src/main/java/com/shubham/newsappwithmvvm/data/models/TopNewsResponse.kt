package com.shubham.newsappwithmvvm.data.models

data class TopNewsResponse(
    val articles: List<TopNewsArticle>?= null,
    val status: String?= null,
    val totalResults: Int? = null
)