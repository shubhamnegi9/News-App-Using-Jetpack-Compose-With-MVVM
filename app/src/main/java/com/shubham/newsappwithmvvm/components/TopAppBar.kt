package com.shubham.newsappwithmvvm.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.shubham.newsappwithmvvm.BottomMenuScreens
import com.shubham.newsappwithmvvm.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarUI(navController: NavController) {
    // navController provides previousBackStackEntry to get the previous screen route
    val previousBackStackEntry = navController.previousBackStackEntry?.destination?.route

    TopAppBar(
        title = {
            Text("Details Screen", fontWeight = FontWeight.SemiBold)
        },
        navigationIcon = {
            IconButton(onClick = {
                // Using navController to navigate to previous screen
                previousBackStackEntry?.let {
                    navController.navigate(it)
                }
            }) {
                Icon(Icons.Default.ArrowBack, "Back")
            }
        },
        colors = TopAppBarColors(
            containerColor = colorResource(R.color.color2),
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            scrolledContainerColor = Color.Transparent,
            actionIconContentColor = Color.Transparent
        )
    )
}