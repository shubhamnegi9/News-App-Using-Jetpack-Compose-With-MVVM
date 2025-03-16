package com.shubham.newsappwithmvvm.data.models

import com.shubham.newsappwithmvvm.R

data class NewsData(
    val id: Int,
    val image:Int = R.drawable.breaking_news,
    val author: String,
    val title: String,
    val description: String,
    val publishedAt: String
)
