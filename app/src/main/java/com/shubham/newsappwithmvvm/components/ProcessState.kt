package com.shubham.newsappwithmvvm.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shubham.newsappwithmvvm.NewsApp
import com.shubham.newsappwithmvvm.R
import com.shubham.newsappwithmvvm.network.ApiUtils
import com.shubham.newsappwithmvvm.viewmodel.MainViewModel

@Composable
fun LoadingUI() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator()     // Shows a circular infinite loader
    }
}

@Composable
fun ErrorUI(errorType: MutableState<String>, navController: NavController) {

    // navController provides currentBackStackEntryAsState() to get the current screen route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            if(errorType.value.equals(ApiUtils.UNKNOWN_HOST_EXCEPTION_ERROR)) {
                Image(
                    painter = painterResource(R.drawable.no_internet),
                    contentDescription = "No Internet",
                    modifier = Modifier.size(300.dp),
                    contentScale = ContentScale.Crop
                )
                Text(ApiUtils.NO_INTERNET_MESSAGE,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Button(modifier = Modifier.padding(top = 10.dp), onClick = {
                    currentRoute?.let {
                        navController.navigate(it)      // Navigate to the current route
                    }
                }) {
                    Text("Retry")
                }
            } else {
                Image(
                    painter = painterResource(R.drawable.generic_error),
                    contentDescription = "No Internet",
                    modifier = Modifier.size(300.dp),
                    contentScale = ContentScale.Crop
                )
                Text(ApiUtils.GENERIC_ERROR_MESSAGE,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold)
                Button(modifier = Modifier.padding(top = 10.dp), onClick = {
                    currentRoute?.let {
                        navController.navigate(it)      // Navigate to the current route
                    }
                }) {
                    Text("Retry")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ErrorUIPreview() {
    val errorType = remember {
        mutableStateOf(ApiUtils.UNKNOWN_HOST_EXCEPTION_ERROR)
    }
    ErrorUI(errorType, rememberNavController())
}