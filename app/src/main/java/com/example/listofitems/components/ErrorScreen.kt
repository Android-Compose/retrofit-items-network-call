package com.example.listofitems.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.listofitems.R

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    errorMessage: String,
    updateErrorMessage: () -> Unit,
    topBar: @Composable () -> Unit,
    updateIsLoading: (Boolean) -> Unit,
) {
    // try ItemUiState here
    Scaffold(
        topBar = topBar ,
        content = { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_connection_error),
                    contentDescription = null
                )
                Text(text = errorMessage, modifier = Modifier.padding(16.dp))
                Button(onClick =  {
                    retryAction()
                    updateErrorMessage()
                    updateIsLoading(false)
                }) {
                    Text(stringResource(id = R.string.retry))
                }
            }
        }
    )
}