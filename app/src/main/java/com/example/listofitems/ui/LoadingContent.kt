package com.example.listofitems.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingContent(
    isEmpty: Boolean,
    onPullRefresh: () -> Unit,
    screenLoading: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if(isEmpty){
        screenLoading()
    } else{
        PullToRefreshBox(
            onPullRefresh = onPullRefresh,
            state = rememberPullToRefreshState(),
            content = content,
        )
    }
}

@Composable
@ExperimentalMaterial3Api
fun PullToRefreshBox(
    onPullRefresh: () -> Unit,
    state: PullToRefreshState,
    content: @Composable () -> Unit
) {
    if (state.isRefreshing) {
        LaunchedEffect(true) {
            delay(1200)
            onPullRefresh()
            state.endRefresh()
        }
    }

    Box(modifier = Modifier.nestedScroll(state.nestedScrollConnection)) {
        content()
        PullToRefreshContainer(
            state = state,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 74.dp)
        )
    }
}