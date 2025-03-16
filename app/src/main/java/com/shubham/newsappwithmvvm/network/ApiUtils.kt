package com.shubham.newsappwithmvvm.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// News API Documentation: https://newsapi.org/docs/endpoints/top-headlines
object ApiUtils {

    private const val BASE_URL = "https://newsapi.org/v2/"
    const val API_KEY = "API_KEY_VALUE"
    const val COUNTRY_US = "us"
    const val UNKNOWN_HOST_EXCEPTION_ERROR = "UnknownHostException"
    const val GENERIC_ERROR = "GenericError"
    const val NO_INTERNET_MESSAGE = "No Network. Please check your internet connection and try again"
    const val GENERIC_ERROR_MESSAGE = "Something Went Wrong. Please Try Again"

    // Moshi converter initialization
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Adding user-agent in header and enable logging of Retrofit calls
    var client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val originalRequest: Request = chain.request()
            val modifiedRequest: Request = originalRequest.newBuilder()
                .addHeader("user-agent", "NewsApp/1.0 (Android; Mobile)")   // Setting a custom User-Agent as default one is causing issue
                // .addHeader("X-Api-key", API_KEY)     // In case we want to pass Api Key in header instead of Query Params
                .build()
            chain.proceed(modifiedRequest)
        }
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))       // enable logging of Retrofit calls
        .build()

    // Initialize Retrofit
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    val newsService : NewsService by lazy {                 // Common practice to use "by lazy"
        retrofitBuilder.create(NewsService::class.java)
    }

}