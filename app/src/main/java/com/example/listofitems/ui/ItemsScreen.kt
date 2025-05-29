package com.example.listofitems.ui


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

/**
 * this composable launches the application
 */
@Composable
fun ItemsScreen() {
    val viewModel: ItemsViewModel = viewModel(factory = ItemsViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState() //ItemUiState

    ItemsWithContent(
        uiState = uiState,
        onRefresh = { viewModel.getItems() },
        retryAction = { viewModel.getItems() },
        updateErrorMessage = viewModel::updateErrorMessage,
        updateIsLoading = viewModel::updateIsLoading,
        updateItems = viewModel::updateItems
    )
}

/**
 * this composable displays screen with items or screen with error
 */
@Composable
fun ItemsWithContent(
    uiState: ItemUiState,
    onRefresh: () -> Unit,
    retryAction: () -> Unit,
    updateErrorMessage: () -> Unit,
    updateIsLoading: (Boolean) -> Unit,
    updateItems: (List<Item>) -> Unit
) {
    Scaffold(
        topBar = { HomeTopBar() },

    ) { innerPadding ->

        LoadingContent(
            isEmpty = when(uiState) {
                is ItemUiState.HasNoItems -> { uiState.loading }
                is ItemUiState.HasItems -> { false }
            },
            onRefresh = onRefresh,
            screenLoading = { ScreenLoading() },
            content = {
                when(uiState) {
                    is ItemUiState.HasItems -> { // has items =  list of items
                        DisplayItems(
                            modifier = Modifier.padding(innerPadding),
                            items = uiState.items,
                            uiState = uiState,
                            retryAction = retryAction,
                            updateErrorMessage = updateErrorMessage,
                            updateIsLoading = updateIsLoading,
                            updateItems = updateItems
                        )
                    }
                    is ItemUiState.HasNoItems -> {
                       // when there is no items and there is an error, show the ErrorScreen
                        if(uiState.errorMessage.isNotEmpty()) {
                            ErrorScreen(
                                uiState = uiState,
                                retryAction = retryAction,
                                topBar = { HomeTopBar() },
                                updateErrorMessage = updateErrorMessage,
                                updateIsLoading = { updateIsLoading(false) }
                            )
                        } else {
                            updateIsLoading(true)
                        }
                    }
                }
            }
        )
    }
}

/**
 * this composable is used to display an error or
 * display the list of items
 */
@Composable
fun DisplayItems(
    modifier: Modifier = Modifier,
    uiState: ItemUiState,
    items: List<Item>,
    updateErrorMessage: () -> Unit,
    retryAction: () -> Unit,
    updateIsLoading: (Boolean) -> Unit,
    updateItems: (List<Item>) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 24.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "ListId")
            Text(text = "Name")
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
        // when the screen has items and somehow there is no internet connection
        // and the user pull to refresh the screen, show error
        if(uiState.errorMessage.isNotEmpty()) {
            ErrorScreen(
                uiState = uiState,
                retryAction = retryAction,
                topBar = {},
                updateErrorMessage = updateErrorMessage,
                updateIsLoading = {
                    updateIsLoading(false)
                    updateItems(emptyList())
                }
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues( horizontal = 24.dp)
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
}