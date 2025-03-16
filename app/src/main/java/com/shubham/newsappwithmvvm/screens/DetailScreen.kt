package com.shubham.newsappwithmvvm.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.shubham.newsappwithmvvm.MockData
import com.shubham.newsappwithmvvm.MockData.getTimeAgo
import com.shubham.newsappwithmvvm.R
import com.shubham.newsappwithmvvm.components.TopAppBarUI
import com.shubham.newsappwithmvvm.data.models.TopNewsArticle
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(paddingVal: PaddingValues, navController: NavController, article: TopNewsArticle) {

    Scaffold(topBar = {
        TopAppBarUI(navController)
    }) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally) {


            /*
            Button(onClick = {
                // Using navController to navigate to TopNews Screen
                navController.navigate("TopNews")
            },
                modifier = Modifier.padding(top = 32.dp)) {
                Text("Go to Top News Screen - ${newsData.author}")
            }
             */

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())) {

                /*
                Image(painter = painterResource(article.image), "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxWidth())
                 */

                // Loading Image using Landscapist Coil Library
                CoilImage(
                    imageModel = { article.urlToImage ?: R.drawable.breaking_news},     // Fallback image in case urlToImage is null
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.FillBounds,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    failure = {
                        Image(
                            painter = painterResource(R.drawable.breaking_news),
                            contentDescription = "Fallback Image",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillBounds
                        )
                    },
                    loading = {
                        Image(
                            painter = painterResource(R.drawable.breaking_news),
                            contentDescription = "Fallback Image",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                )

                Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp)) {

                    Row(modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, "author", tint = colorResource(R.color.color1),
                            modifier = Modifier.padding(end = 8.dp))
                        Text(article.author ?: "Not Available", fontWeight = FontWeight.SemiBold)
                    }

                    // String timestamp to timeAgo
                    val timeAgo = article.publishedAt?.let { MockData.stringToDate(it).getTimeAgo() }
                    if (timeAgo != null) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 4.dp)) {
                            Icon(Icons.Default.DateRange, "author", tint = colorResource(R.color.color1),
                                modifier = Modifier.padding(end = 8.dp))
                            Text(timeAgo, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Text(article.title ?: "Not Available", Modifier.padding(8.dp), fontWeight = FontWeight.SemiBold)
                Text(article.description ?: "", Modifier.padding(8.dp))

                val uriHandler = LocalUriHandler.current
                val annotatedString = buildAnnotatedString {
                    pushStringAnnotation(
                        tag = "URL",
                        annotation = article.url ?: "newsapi.org"
                    )
                    withStyle(style = SpanStyle(
                        color = colorResource(R.color.purple_500),
                        textDecoration = TextDecoration.Underline
                    )
                    ) {
                        append("Read Full Article Here")
                    }
                }
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

@Preview(showBackground = true)
@Composable
fun DetainScreenPreview() {
    DetailScreen(PaddingValues.Absolute(16.dp)
        , rememberNavController(),
        TopNewsArticle(
            author = "Kara Fox, Zahid Mahmood, Kathleen Magramo",
            title = "'Tiger King' Joe Exotic says he has been diagnosed with aggressive form of prostate cancer - CNN",
            description = "Joseph Maldonado, known as Joe Exotic on the 2020 Netflix docuseries \\\"Tiger King: Murder, Mayhem and Madness,\\\" has been diagnosed with an aggressive form of prostate cancer, according to a letter written by Maldonado.",
            publishedAt = "2021-11-04T05:35:21Z"
        )
    )
}