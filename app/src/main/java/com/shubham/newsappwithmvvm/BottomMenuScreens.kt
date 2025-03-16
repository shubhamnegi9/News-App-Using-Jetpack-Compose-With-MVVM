package com.shubham.newsappwithmvvm

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Source
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomMenuScreens(val route: String, val icon: ImageVector, val title: String) {

    object TopNews: BottomMenuScreens("top news", Icons.Outlined.Home, "Top News")

    object Categories: BottomMenuScreens("categories", Icons.Outlined.Category, "Categories")

    object Sources: BottomMenuScreens("sources", Icons.Outlined.Source, "Sources")

}