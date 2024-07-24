package com.example.listofitems.ui


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listofitems.R
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
        updateErrorMessage = viewModel::updateErrorMessage
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
                        Log.d("HasState", "hasState: $uiState")
                        DisplayItems(
                            modifier = Modifier.padding(innerPadding),
                            items = uiState.items,
                            uiState = uiState,
                            retryAction = retryAction,
                            errorMessage = uiState.errorMessage,
                            updateErrorMessage = updateErrorMessage
                        )
                    }
                    is ItemUiState.HasNoItems -> {
                        Log.d("hasNoState", "hasNoState: $uiState")
                        // when there is no items and there is an error, show the ErrorScreen
                        if(uiState.errorMessage.isNotEmpty()) {
                            ErrorScreen(
                                errorMessage = uiState.errorMessage,
                                retryAction = retryAction,
                                topBar = { HomeTopBar() },
                                updateErrorMessage = updateErrorMessage
                            )
                        } else {
                            // if there is no error and there are no items, let the user refresh
                            TextButton(
                                onClick = onRefresh,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = stringResource( R.string.tap_to_load_content),
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                )
                            }
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
    errorMessage: String,
    uiState: ItemUiState,
    items: List<Item>,
    updateErrorMessage: () -> Unit,
    retryAction: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 32.dp, end = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "ListId")
            Text(text = "Name")
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 32.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
        // if screen has items and somehow there is an error, show the error on screen on pullRefresh
        if(uiState.errorMessage.isNotEmpty()) {
            ErrorScreen(
                retryAction = retryAction,
                errorMessage = errorMessage,
                topBar = {},
                updateErrorMessage = updateErrorMessage
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues( horizontal = 32.dp)
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

// Log.d("isRetry", "isRetry: ${uiState.isRetry}")
//                        Log.d("hasNoState ScreenLoading", "hasNoState ScreenLoading: $uiState")
//                        if(!uiState.isRetry && uiState.errorMessage.isNotEmpty()){
//                            Log.d("error message", "message: ${uiState.errorMessage}")
//                            ScreenLoading()
//                        }  else if(uiState.isRetry && uiState.errorMessage.isEmpty()) {
//                            Log.d("error message", "message: ${uiState.errorMessage}")
//                            ScreenLoading()
//                        }