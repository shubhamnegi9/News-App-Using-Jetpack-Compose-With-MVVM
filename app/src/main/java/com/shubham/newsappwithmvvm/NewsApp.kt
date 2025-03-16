package com.shubham.newsappwithmvvm

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shubham.newsappwithmvvm.components.BottomMenu
import com.shubham.newsappwithmvvm.data.models.ArticleCategory
import com.shubham.newsappwithmvvm.data.models.TopNewsArticle
import com.shubham.newsappwithmvvm.network.NewsManager
import com.shubham.newsappwithmvvm.screens.Categories
import com.shubham.newsappwithmvvm.screens.DetailScreen
import com.shubham.newsappwithmvvm.screens.Sources
import com.shubham.newsappwithmvvm.screens.TopNews
import com.shubham.newsappwithmvvm.viewmodel.MainViewModel

@Composable
fun NewsApp(viewModel: MainViewModel) {

    val articles = mutableListOf<TopNewsArticle>()
    articles.addAll(viewModel.newsResponse.collectAsState().value.articles ?: listOf(TopNewsArticle()))     // Collects values from this StateFlow and represents its latest value via State
    Log.d("NewsArticles", "NewsArticles: $articles")

    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.isError.collectAsState()
    val errorType by viewModel.errorType.collectAsState()

    val navController = rememberNavController()

    // navController provides currentBackStackEntryAsState() to get the current screen route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("CurrentRoute", "currentRoute: $currentRoute")

    // Shows bottom bar only on selected list of routes
    val bottomBarVisible = currentRoute in listOf(
        BottomMenuScreens.TopNews.route,
        BottomMenuScreens.Categories.route,
        BottomMenuScreens.Sources.route
    )

    Scaffold(bottomBar = {
        if(bottomBarVisible)
            BottomMenu(navController, currentRoute)
    }) { innerPadding ->

        if (articles != null) {
            Navigation(innerPadding, navController, articles, viewModel, loading, error, errorType)
        }
    }
}

@Composable
fun Navigation(innerPadding: PaddingValues, navController: NavHostController, articles: MutableList<TopNewsArticle>,
               viewModel: MainViewModel, loading: Boolean, error: Boolean, errType: String) {


    NavHost(
        navController = navController,
        startDestination = BottomMenuScreens.TopNews.route    // startDestination represents first screen that will be shown after launching app
    ) {

        val isLoading = mutableStateOf(loading)
        val isError = mutableStateOf(error)
        val errorType = mutableStateOf(errType)

        // Add the bottom navigation to the Navigation graph
        bottomNavigation(innerPadding, navController, articles, viewModel, isLoading, isError, errorType)

        /*
            Commented as it is called from bottomNavigation

            composable("TopNews") {
                TopNews(innerPadding, navController)
            }
         */

        composable("Details/{index}/{source}",                              // added a placeholder for the argument to be received
            arguments = listOf(
                navArgument("index"){ type = NavType.IntType },                                   // provide the argument type using NavType
                navArgument("source"){ type = NavType.StringType }
            )) {    navBackStackEntry ->
                // receive the index from NavBackStackEntry, we use getInt since its of Int type and pass in the key
                val index = navBackStackEntry.arguments?.getInt("index")
                // pass in the id to getNewsDataFromId method created in MockData to retrieve selected news
                // val newsData = MockData.getNewsDataFromId(newsId)

                val source = navBackStackEntry.arguments?.getString("source")

                index?.let {
                    val topArticles = mutableListOf<TopNewsArticle>()
                    // If search text query is not empty, then show the articles from search text query otherwise show all the articles
                    if(viewModel.searchQuery.collectAsState().value != "") {
                        topArticles.clear()
                        topArticles.addAll(viewModel.searchQueryResponse.collectAsState().value.articles ?: listOf())  // With StateFlow, we need to get the value as collectAsState().value
                    } else {
                        topArticles.clear()
                        if(source?.equals(BottomMenuScreens.TopNews.route) == true) {
                            topArticles.addAll(viewModel.newsResponse.collectAsState().value.articles ?: listOf())     // With StateFlow, we need to get the value as collectAsState().value
                        } else {
                            topArticles.addAll(viewModel.articleByCategory.collectAsState().value.articles ?: listOf())
                        }
                    }
                    val article = topArticles[index]
                    // provide newsData as a value to detail screen
                    DetailScreen(innerPadding, navController, article)
                }

        }

    }
}

fun NavGraphBuilder.bottomNavigation(innerPadding: PaddingValues, navController: NavController, articles: List<TopNewsArticle>,
                                     viewModel: MainViewModel, isLoading: MutableState<Boolean>, isError: MutableState<Boolean>,
                                     errorType: MutableState<String>) {
    composable(BottomMenuScreens.TopNews.route) {
        /*
            Since the composable lambda gets re-executed multiple times, the API call will also be made multiple times here leading to redundant network requests.
            To ensure the API call is made only once per navigation, wrap it inside LaunchedEffect.
            LaunchedEffect(Unit) ensures that the block inside runs only once when the Categories composable is first created.
            rememberSaveable ensures that the value persists even if the app is recreated due to configuration.
         */
        val hasFetched = rememberSaveable { mutableStateOf(false) }
        val hasErrorOccurred = rememberSaveable { mutableStateOf(false) }

        if(isError.value) {
            hasErrorOccurred.value = true
        }

        LaunchedEffect(Unit) {
            if(!hasFetched.value || hasErrorOccurred.value) {   // In case error has occurred, we need to call API again
                hasFetched.value = true
                hasErrorOccurred.value = false
                // Getting Top News Articles using viewModel
                viewModel.getArticles()
            }
        }
        TopNews(innerPadding, navController, articles, viewModel, isLoading, isError, errorType)
    }
    composable(BottomMenuScreens.Categories.route) {
        val hasFetched = rememberSaveable { mutableStateOf(false) }
        val hasErrorOccurred = rememberSaveable { mutableStateOf(false) }

        if(isError.value) {
            hasErrorOccurred.value = true
        }

        LaunchedEffect(Unit) {
            if(!hasFetched.value || hasErrorOccurred.value) {
                hasFetched.value = true
                hasErrorOccurred.value = false
                viewModel.getArticlesByCategory(ArticleCategory.BUSINESS.categoryName)    // Getting articles for "business" category if none of tabs are selected initially
            }
        }
        Categories(innerPadding, navController, viewModel, isLoading, isError, errorType)
    }
    composable(BottomMenuScreens.Sources.route) {
        val hasFetched = rememberSaveable { mutableStateOf(false) }
        val hasErrorOccurred = rememberSaveable { mutableStateOf(false) }

        if(isError.value) {
            hasErrorOccurred.value = true
        }
        LaunchedEffect(Unit) {
            if(!hasFetched.value || hasErrorOccurred.value) {
                hasFetched.value = true
                hasErrorOccurred.value = false
                viewModel.getArticlesBySources()
            }
        }
        Sources(innerPadding, navController, viewModel, isLoading, isError, errorType)
    }
}