package com.shubham.newsappwithmvvm

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.shubham.newsappwithmvvm.network.ApiUtils
import com.shubham.newsappwithmvvm.network.NewsManager
import com.shubham.newsappwithmvvm.ui.theme.NewsAppTheme
import com.shubham.newsappwithmvvm.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initializing MainViewModel
        val mainViewModel by viewModels<MainViewModel>()

        setContent {
            NewsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NewsApp(mainViewModel)
                }
            }
        }
    }
}
