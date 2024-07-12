package com.example.listofitems.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
    errorMessage: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(text = errorMessage, modifier = Modifier.padding(16.dp))
        Button(onClick =  retryAction ) {
            Text(stringResource(id = R.string.retry))
        }
    }
}

//@Composable
//fun ShowError(errorMessage: String) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement= Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Icon(
//            imageVector = Icons.Filled.Error,
//            contentDescription = "Error",
//            tint = Color.Red,
//            modifier = Modifier.size(48.dp))
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(
//            text = "Oops! Something went wrong.",
//            style = MaterialTheme.typography.h6,
//            color = Color.Red
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Text(
//            text = errorMessage,
//            style = MaterialTheme.typography.body1
//        )
//        // You can add a "Retry" button or other actions here if needed
//    }
//}