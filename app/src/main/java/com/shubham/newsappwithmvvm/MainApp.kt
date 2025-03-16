package com.shubham.newsappwithmvvm

import android.app.Application
import com.shubham.newsappwithmvvm.data.Repository
import com.shubham.newsappwithmvvm.network.ApiUtils
import com.shubham.newsappwithmvvm.network.NewsManager

/*
    In this application class, we're going to initialize the newsManager first well as the repository needs it.
    When we initialize the repository providing the manager as an argument, we use "by lazy" to ensure they
    are only initialized at the point of use and not directly when the MainApp is created.
 */
class MainApp: Application() {

    private val newsManager by lazy {
        NewsManager(ApiUtils.newsService)
    }

    val repository by lazy {
        Repository(newsManager)
    }
}