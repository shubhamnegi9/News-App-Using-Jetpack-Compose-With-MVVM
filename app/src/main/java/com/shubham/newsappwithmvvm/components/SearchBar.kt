package com.shubham.newsappwithmvvm.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shubham.newsappwithmvvm.R
import com.shubham.newsappwithmvvm.data.models.TopNewsResponse
import com.shubham.newsappwithmvvm.network.NewsManager
import com.shubham.newsappwithmvvm.viewmodel.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SearchBar(query: MutableStateFlow<String>, viewModel: MainViewModel) {
    val localFocusManager = LocalFocusManager.current
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    val imeVisible by rememberUpdatedState(imeBottom > 0)

    // Clear focus when keyboard disappears
    LaunchedEffect(imeVisible) {
        if (!imeVisible) {
            localFocusManager.clearFocus()
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(colorResource(R.color.color1))
    ) {
        TextField(
            value = query.collectAsState().value,
            onValueChange = {
                query.value = it
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
            label = {Text("Search News", color = Color.White)},
            colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent),
            leadingIcon = {Icon(Icons.Filled.Search, "Search", tint = Color.White)},
            trailingIcon = {
                if(query.collectAsState().value != "") {
                    IconButton(onClick = {
                        query.value = ""
                        // Initializing with empty TopNewsResponse
                        viewModel.searchQueryResponse = MutableStateFlow(TopNewsResponse())
                        localFocusManager.clearFocus()
                    }) {
                        Icon(Icons.Default.Clear, "Clear", tint = Color.White)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search),           // Shows Search Icon in Keyboard
            keyboardActions = KeyboardActions(
                onSearch = {
                    if(query.value != "") {
                        viewModel.getArticlesByQuery(query.value)
                    }
                    localFocusManager.clearFocus()
                },
            )
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchBarPreview(modifier: Modifier = Modifier) {
    SearchBar(MutableStateFlow(""), viewModel())
}