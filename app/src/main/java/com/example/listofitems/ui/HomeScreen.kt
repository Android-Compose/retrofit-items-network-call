package com.example.listofitems.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listofitems.components.ErrorScreen
import com.example.listofitems.components.HomeTopBar
import com.example.listofitems.components.ScreenLoading
import com.example.listofitems.model.Item


@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState() //ItemUiState

    HomeWithContent(
        uiState = uiState,
        onPullRefresh = {},
        retryAction = {}
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeWithContent(
    uiState: ItemUiState,
    onPullRefresh: () -> Unit,
    retryAction: () -> Unit
) {

    Scaffold(
        topBar = { HomeTopBar() },

    ) { innerPadding ->

        LoadingContent(
            isEmpty = when(uiState) {
                is ItemUiState.HasNoItems -> { uiState.loading }
                is ItemUiState.HasItems -> { false }
            },
            onPullRefresh = onPullRefresh,
            screenLoading = { ScreenLoading() },
            content = {
                when(uiState) {
                    is ItemUiState.HasItems -> {
                        DisplayItems(
                            modifier = Modifier.padding(innerPadding),
                            items = uiState.items)

                    }
                    is ItemUiState.HasNoItems -> {
                       ErrorScreen(
                           errorMessage = uiState.errorMessage,
                           retryAction = retryAction
                       )
                    }
                }
            }
        )
    }
}

@Composable
fun DisplayItems(
    modifier: Modifier = Modifier,
    items: List<Item>
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top= 24.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "ListId")
            Text(text = "Name")
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        LazyColumn(
            contentPadding = PaddingValues( horizontal = 16.dp)
        ) {
            items(items) { item ->
                Row( modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item.listId )
                    Text(item.name ?: "no name")
                }
            }
        }
    }
}