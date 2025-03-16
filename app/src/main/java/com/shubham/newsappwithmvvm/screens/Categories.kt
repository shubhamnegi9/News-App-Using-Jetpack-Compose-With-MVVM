package com.shubham.newsappwithmvvm.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.shubham.newsappwithmvvm.BottomMenuScreens
import com.shubham.newsappwithmvvm.MockData
import com.shubham.newsappwithmvvm.MockData.getTimeAgo
import com.shubham.newsappwithmvvm.R
import com.shubham.newsappwithmvvm.components.ErrorUI
import com.shubham.newsappwithmvvm.components.LoadingUI
import com.shubham.newsappwithmvvm.data.models.TopNewsArticle
import com.shubham.newsappwithmvvm.data.models.getAllArticleCategories
import com.shubham.newsappwithmvvm.network.NewsManager
import com.shubham.newsappwithmvvm.viewmodel.MainViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage


@Composable
fun Categories(innerPadding: PaddingValues, navController: NavController, viewModel: MainViewModel,
               isLoading: MutableState<Boolean>, isError: MutableState<Boolean>, errorType: MutableState<String>) {
    val categoryTabs = getAllArticleCategories()

    when {
        isLoading.value ->
            LoadingUI()
        isError.value ->
            ErrorUI(errorType, navController)
        else -> {
            Column(modifier = Modifier.padding(innerPadding)) {
                LazyRow {
                    items(categoryTabs.size) {
                        val category = categoryTabs[it]
                        CategoryTab(
                            categoryName = category.categoryName,
                            isSelected = viewModel.selectedCategory.collectAsState().value == category,         // With StateFlow, we need to get the value as collectAsState().value
                            onCategorySelected = {
                                viewModel.onSelectedCategoryChanged(it)
                                viewModel.getArticlesByCategory(it)
                            }
                        )
                    }
                }

                viewModel.articleByCategory.collectAsState().value.articles?.let {
                    ArticleContent(it, navController)
                }
            }
        }
    }

}

@Composable
fun CategoryTab(categoryName: String, isSelected: Boolean = false, onCategorySelected: (String) -> Unit) {

    val backgroundColor = if(isSelected) colorResource(R.color.color1) else colorResource(R.color.color2)

    Surface(
        color = backgroundColor,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            onCategorySelected(categoryName)
        }
    ) {
       Text(categoryName,
           color = Color.White,
           modifier = Modifier.padding(8.dp))
    }

}

@Composable
fun ArticleContent(topNewsArticles: List<TopNewsArticle>, navController: NavController) {
    LazyColumn {
        items(topNewsArticles.size) { index ->
            val article = topNewsArticles[index]
            Card(
                modifier = Modifier.padding(8.dp),
                border = BorderStroke(2.dp, colorResource(R.color.color2)),
                elevation = CardDefaults.cardElevation(4.dp),
                onClick = {
                    // Navigate to Details Screen
                    navController.navigate("Details/${index}/${BottomMenuScreens.Categories.route}")
                }
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    CoilImage(
                        imageModel = {article.urlToImage ?: R.drawable.breaking_news},
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                        ),
                        modifier = Modifier.size(100.dp),
                        failure = {
                            Image(
                                painter = painterResource(R.drawable.breaking_news),
                                contentDescription = "Fallback Image",
                                modifier = Modifier.size(100.dp),
                                contentScale = ContentScale.Crop
                            )
                        },
                        loading = {
                            Image(
                                painter = painterResource(R.drawable.breaking_news),
                                contentDescription = "Fallback Image",
                                modifier = Modifier.size(100.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    )

                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = article.title ?: "Not Available",
                            fontWeight = FontWeight.Bold,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)) {

                            Text(text = article.author ?: "Anonymous",
                                modifier = Modifier.weight(1f),
                                fontSize = 12.sp)
                            article.publishedAt?.let {
                                Text(text = MockData.stringToDate(it).getTimeAgo(),
                                    fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ArticleContentPreview(modifier: Modifier = Modifier) {
    ArticleContent(listOf(
        TopNewsArticle(author = "REINHARDT KRAUSE, Investor's Business Daily",
            title = "'Tiger King' Joe Exotic says he has been diagnosed with aggressive form of prostate cancer - CNN",
            description = "Joseph Maldonado, known as Joe Exotic on the 2020 Netflix docuseries \\\"Tiger King: Murder, Mayhem and Madness,\\\" has been diagnosed with an aggressive form of prostate cancer, according to a letter written by Maldonado.",
            publishedAt = "2025-02-21T05:35:21Z",
            urlToImage = null)
    ), rememberNavController())
}
