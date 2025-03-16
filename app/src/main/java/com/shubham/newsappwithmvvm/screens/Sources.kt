package com.shubham.newsappwithmvvm.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.shubham.newsappwithmvvm.R
import com.shubham.newsappwithmvvm.components.ErrorUI
import com.shubham.newsappwithmvvm.components.LoadingUI
import com.shubham.newsappwithmvvm.data.models.TopNewsArticle
import com.shubham.newsappwithmvvm.network.NewsManager
import com.shubham.newsappwithmvvm.viewmodel.MainViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sources(innerPadding: PaddingValues, navController: NavController, viewModel: MainViewModel,
            isLoading: MutableState<Boolean>, isError: MutableState<Boolean>, errorType: MutableState<String>) {

    // List of Pairs to be shown in DropDown Menu
    val items = listOf(
        "abcNews" to "abc-news",
        "TechCrunch" to "techcrunch",
        "TalkSport" to "talksport",
        "Business Insider" to "business-insider",
        "Reuters" to "reuters",
        "Politico" to "politico",
        "TheVerge" to "the-verge"
    )

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text("${viewModel.sourceName.collectAsState().value} Source", fontWeight = FontWeight.SemiBold)
            },
            colors = TopAppBarColors(
                containerColor = colorResource(R.color.color2),
                navigationIconContentColor = Color.White,
                titleContentColor = Color.White,
                scrolledContainerColor = Color.Transparent,
                actionIconContentColor = Color.Transparent
            ),
            actions = {
                var isMenuExpanded by remember {
                    mutableStateOf(false)
                }
                IconButton(onClick = {
                    isMenuExpanded = true
                }) {
                    Icon(Icons.Default.MoreVert, "More",
                        tint = Color.White)
                }
                MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp))) {     // Adding Rounded Corner Shape to DropDown Menu
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = {
                            isMenuExpanded = false
                        }
                    ) {
                        items.forEach { (key, value) ->
                            DropdownMenuItem(
                                text = { Text(key) },
                                onClick = {
                                    viewModel.sourceName.value = value
                                    viewModel.getArticlesBySources()
                                    isMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        )
    }
    ) { innerPadding ->

            when {
                isLoading.value ->
                    LoadingUI()
                isError.value ->
                    ErrorUI(errorType, navController)
                else -> {
                    val articleList = viewModel.articleBySources.collectAsState().value.articles
                    if (articleList != null) {
                        SourcesContent(articleList,innerPadding)
                    }
                }
            }
    }

}

@Composable
fun SourcesContent(articles: List<TopNewsArticle>, innerPadding: PaddingValues) {

    val uriHandler = LocalUriHandler.current

    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(articles) { article ->

            val annotatedString = buildAnnotatedString {
                pushStringAnnotation(
                    tag = "URL",
                    annotation = article.url ?: "newsapi.org"
                )
                withStyle(style = SpanStyle(
                    color = colorResource(R.color.purple_500),
                    textDecoration = TextDecoration.Underline
                )) {
                    append("Read Full Article Here")
                }
            }

            Card(
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CoilImage(
                        imageModel = {article.urlToImage ?: R.drawable.breaking_news},
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                        ),
                        modifier = Modifier.height(200.dp).fillMaxWidth(),
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
                    Column(
                        modifier = Modifier.height(200.dp).background(Color.Black.copy(alpha = 0.5f)),      // Adding a black gradient overlay
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = article.title ?: "Not Available",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
                        )
                        Text(
                            text = article.description ?: "Not Available",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                        )
                        Card(
                            colors = CardDefaults.cardColors(colorResource( R.color.white)),
                            elevation = CardDefaults.cardElevation(6.dp),
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        ) {
                            Text(text = annotatedString,
                                modifier = Modifier.padding(8.dp).clickable {
                                    annotatedString.getStringAnnotations(start = 0, end = annotatedString.length).firstOrNull()?.let {
                                            result ->
                                        if(result.tag == "URL") {
                                            uriHandler.openUri(result.item)
                                        }
                                    }
                                })
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SourcesContentPreview() {
    SourcesContent(
        listOf(
            TopNewsArticle(author = "REINHARDT KRAUSE, Investor's Business Daily",
                title = "'Tiger King' Joe Exotic says he has been diagnosed with aggressive form of prostate cancer - CNN",
                description = "Joseph Maldonado, known as Joe Exotic on the 2020 Netflix docuseries \\\"Tiger King: Murder, Mayhem and Madness,\\\" has been diagnosed with an aggressive form of prostate cancer, according to a letter written by Maldonado.",
                publishedAt = "2025-02-21T05:35:21Z",
                urlToImage = "https://s.abcnews.com/images/US/abc_news_default_2000x2000_update_16x9_992.jpg")
        ), PaddingValues(8.dp)
    )
}