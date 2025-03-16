package com.shubham.newsappwithmvvm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shubham.newsappwithmvvm.MainApp
import com.shubham.newsappwithmvvm.data.models.ArticleCategory
import com.shubham.newsappwithmvvm.data.models.TopNewsResponse
import com.shubham.newsappwithmvvm.data.models.getArticleCategory
import com.shubham.newsappwithmvvm.network.ApiUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = getApplication<MainApp>().repository

    private val _newsResponse = MutableStateFlow(TopNewsResponse())                         // private variable only used in this class
    val newsResponse: StateFlow<TopNewsResponse>                                            // Getter of _newsResponse private variable
        get() = _newsResponse

    private val _articleByCategory = MutableStateFlow(TopNewsResponse())                    // private variable only used in this class
    val articleByCategory: StateFlow<TopNewsResponse>                                       // Getter of _articleByCategory private variable
        get() = _articleByCategory

    private val _articleBySources = MutableStateFlow(TopNewsResponse())                    // private variable only used in this class
    val articleBySources: StateFlow<TopNewsResponse>                                       // Getter of _articleBySources private variable
        get() = _articleBySources

    private var _searchQueryResponse: MutableStateFlow<TopNewsResponse> = MutableStateFlow(TopNewsResponse())                    // private variable only used in this class
    var searchQueryResponse: StateFlow<TopNewsResponse>                                       // Getter of _searchQueryResponse private variable
        get() = _searchQueryResponse
        set(value) {
            _searchQueryResponse = value as MutableStateFlow<TopNewsResponse>
        }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean>
        get() = _isError

    private val _errorType = MutableStateFlow("")
    val errorType: StateFlow<String>
        get() = _errorType

    val selectedCategory: MutableStateFlow<ArticleCategory?> = MutableStateFlow(ArticleCategory.BUSINESS)     // Selected category tab as "business" by default
    val sourceName: MutableStateFlow<String> = MutableStateFlow("abc-news")
    val searchQuery: MutableStateFlow<String> = MutableStateFlow("")


    // Error Handler
    val errorHandler = CoroutineExceptionHandler {
        context, throwable ->
            if(throwable is UnknownHostException) {     // No Internet Connection
                _isError.value = true
                _errorType.value = ApiUtils.UNKNOWN_HOST_EXCEPTION_ERROR
                _isLoading.value = false
            }
            else if(throwable is Exception) {                // Generic Error
                _isError.value = true
                _errorType.value = ApiUtils.GENERIC_ERROR
                _isLoading.value = false
            }
    }

    fun getArticles() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {      // Adding errorHandler as a context inside CoroutineScope.launch along with Dispatcher.IO
            _newsResponse.value = repository.getArticles()
            _isLoading.value = false        // Making _isLoading as false inside viewModelScope after receiving the response
            _isError.value = false
        }
    }

    fun getArticlesByCategory(category: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _articleByCategory.value = repository.getArticlesByCategory(category)
            _isLoading.value = false
            _isError.value = false
        }
    }

    fun getArticlesBySources() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _articleBySources.value = repository.getArticlesBySources(sourceName.value)
            _isLoading.value = false
            _isError.value = false
        }
    }

    fun getArticlesByQuery(query: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _searchQueryResponse.value = repository.getArticlesByQuery(query)
            _isLoading.value = false
            _isError.value = false
        }
    }

    fun onSelectedCategoryChanged(categoryName: String) {
        val newCategory = getArticleCategory(categoryName)
        selectedCategory.value = newCategory
    }
}