package com.shubham.newsappwithmvvm.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.shubham.newsappwithmvvm.BottomMenuScreens
import com.shubham.newsappwithmvvm.R

@Composable
fun BottomMenu(navController: NavHostController, currentRoute: String?) {

    val menuItems = listOf(
        BottomMenuScreens.TopNews,
        BottomMenuScreens.Categories,
        BottomMenuScreens.Sources
    )

    NavigationBar(
        containerColor = colorResource(R.color.color2)
    ) {
        menuItems.forEach {
            NavigationBarItem(
                label = { Text(it.title) },
                icon = { Icon(it.icon, "")},
                selected = it.route == currentRoute,
                alwaysShowLabel = true,
                onClick = {
                    navController.navigate(it.route) {  // Navigate to the given route

                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {    // Pop up to the start destination
                                saveState = true    // Save state of the previous destination
                            }
                        }
                        launchSingleTop = true  // Avoid creating multiple instances of the same destination
                        restoreState = true     // Restore the previous state if available
                    }
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    selectedIndicatorColor = Color.Transparent,
                    disabledIconColor = Color.Unspecified,
                    disabledTextColor = Color.Unspecified
                )
            )
        }
    }
}
