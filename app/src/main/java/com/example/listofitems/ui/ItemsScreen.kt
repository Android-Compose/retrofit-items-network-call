package com.example.listofitems.ui


import android.util.Log
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    updateItems: (Map<Int, List<Item>>) -> Unit
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
                        ComposeContent(
                            modifier = Modifier.padding(innerPadding),
                            sortedItems = uiState.items,
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
fun ComposeContent(
    modifier: Modifier = Modifier,
    uiState: ItemUiState,
    sortedItems: Map<Int, List<Item>>,
    updateErrorMessage: () -> Unit,
    retryAction: () -> Unit,
    updateIsLoading: (Boolean) -> Unit,
    updateItems: (Map<Int, List<Item>>) -> Unit
) {
    // if there is an error display it
    if (uiState.errorMessage.isNotEmpty()) {
        ErrorScreen(
            uiState = uiState,
            retryAction = retryAction,
            topBar = {},
            updateErrorMessage = updateErrorMessage,
            updateIsLoading = {
                updateIsLoading(false)
                updateItems(mapOf())
            }
        )
        return
    }

    if (sortedItems.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "No data to show")
        }
        return
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Spacer(modifier = Modifier.height(104.dp))
        sortedItems.forEach { (listId, names) ->
            Card(
                modifier = Modifier.padding(16.dp),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "List Id: $listId",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold)

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    // display the names
                    names.chunked(4).forEach { row ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end=16.dp
                                )
                        ) {
                            row.forEach {
                                Text(text = it.name ?: "no name")
                            }
                        }
                    }
                }
            }
        }
    }
}
