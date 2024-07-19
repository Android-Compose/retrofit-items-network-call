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

@Composable
fun ItemsWithContent(
    uiState: ItemUiState,
    onRefresh: () -> Unit,
    retryAction: () -> Unit,
    updateErrorMessage: () -> Unit
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
                            errorMessage = uiState.errorMessage,
                            updateErrorMessage = updateErrorMessage
                        )
                    }
                    is ItemUiState.HasNoItems -> {
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

@Composable
fun DisplayItems(
    modifier: Modifier = Modifier,
    errorMessage: String,
    uiState: ItemUiState,
    items: List<Item>,
    updateErrorMessage: () -> Unit,
    retryAction: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "ListId")
            Text(text = "Name")
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        if(uiState.errorMessage.isNotEmpty()) {
            ErrorScreen(
                retryAction = retryAction,
                errorMessage = errorMessage,
                topBar = { },
                updateErrorMessage = updateErrorMessage
            )
        } else {
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
}