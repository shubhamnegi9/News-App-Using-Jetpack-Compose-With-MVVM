package com.shubham.newsappwithmvvm.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shubham.newsappwithmvvm.BottomMenuScreens
import com.shubham.newsappwithmvvm.MockData
import com.shubham.newsappwithmvvm.MockData.getTimeAgo
import com.shubham.newsappwithmvvm.R
import com.shubham.newsappwithmvvm.components.ErrorUI
import com.shubham.newsappwithmvvm.components.LoadingUI
import com.shubham.newsappwithmvvm.components.SearchBar
import com.shubham.newsappwithmvvm.data.models.TopNewsArticle
import com.shubham.newsappwithmvvm.viewmodel.MainViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun TopNews(innerPadding: PaddingValues, navController: NavController, articles: List<TopNewsArticle>,
            viewModel: MainViewModel, isLoading: MutableState<Boolean>, isError: MutableState<Boolean>,
            errorType: MutableState<String>) {

    Column(modifier = Modifier
        .padding(innerPadding)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        /*
        Text("Top News", fontWeight = FontWeight.SemiBold)

        Button(onClick = {
            navController.popBackStack()    // Exits the application on clicking back from TopNews screen
            // Using navController to navigate to Details Screen
            navController.navigate("Details")
        },
            modifier = Modifier.padding(top = 32.dp)) {
            Text("Go to Details Screen")
        }
         */

        // Show Search Bar Composable at top of TopNews Section
        SearchBar(viewModel.searchQuery, viewModel)

        val searchText = viewModel.searchQuery.collectAsState().value
        val resultList = mutableListOf<TopNewsArticle>()

        Log.d("SearchText", "searchText: " + searchText)
        // If search text query is not empty, then show the articles from search text query otherwise show all the articles
        if(searchText != "") {
            Log.d("SearchQueryResponse", "TopNews: " + viewModel.searchQueryResponse.collectAsState().value)
            resultList.addAll(viewModel.searchQueryResponse.collectAsState().value.articles ?: articles)
        } else {
            resultList.addAll(articles)
        }

        when {
            isLoading.value ->
                LoadingUI()
            isError.value ->
                ErrorUI(errorType, navController)
            else -> {
                LazyColumn {
                    items(resultList.size) { index ->         // Gives index of current article
                        // Passed onClick lambda function as argument
                        TopNewsItem(resultList[index], onClick = {
                            // Functionality when onClick lambda function is called

                            // Passed newsData.id as argument to Details Screen instead of newsData
                            // as NewsData data class is not serializable.
                            // Also passing the source route.
                            navController.navigate("Details/${index}/${BottomMenuScreens.TopNews.route}")
                        })
                    }
                }
            }
        }

    }
}

@Composable
fun TopNewsItem(article: TopNewsArticle, onClick: () -> Unit) {
    Card(modifier = Modifier
        .height(200.dp)
        .padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        onClick = {
            // Call lambda function onClick() here
            onClick()
        }
    ) {
        /*
            Image(painter = painterResource(newsData.image), "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxWidth())
         */

        Box(modifier = Modifier.fillMaxSize()) {
            // Loading Image using Landscapist Coil Library
            CoilImage(
                imageModel = {
                    article.urlToImage ?: R.drawable.breaking_news
                },     // Fallback image in case urlToImage is null
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                ),
                modifier = Modifier.fillMaxWidth(),
                failure = {
                    Image(
                        painter = painterResource(R.drawable.breaking_news),
                        contentDescription = "Fallback Image",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                },
                loading = {
                    Image(
                        painter = painterResource(R.drawable.breaking_news),
                        contentDescription = "Fallback Image",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            )

            Column(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.5f))      // Adding a black gradient overlay
                    .matchParentSize()
                    .padding(top = 16.dp, start = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // String timestamp to timeAgo
                article.publishedAt?.let {
                    val timeAgo = MockData.stringToDate(it).getTimeAgo()
                    Text(timeAgo, color = Color.White, fontWeight = FontWeight.SemiBold)
                }
                article.title?.let {
                    Text(it, color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopNewsPreview() {
//    TopNews(PaddingValues.Absolute(16.dp), rememberNavController())

    TopNewsItem(
        TopNewsArticle(
        author = "Raja Razek, CNN",
        title = "'Tiger King' Joe Exotic says he has been diagnosed with aggressive form of prostate cancer - CNN",
        description = "Joseph Maldonado, known as Joe Exotic on the 2020 Netflix docuseries \\\"Tiger King: Murder, Mayhem and Madness,\\\" has been diagnosed with an aggressive form of prostate cancer, according to a letter written by Maldonado.",
        publishedAt = "2021-11-04T05:35:21Z"
    ), {})
}