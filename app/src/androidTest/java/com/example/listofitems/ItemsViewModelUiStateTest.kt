package com.example.listofitems


import com.example.listofitems.data.Result
import com.example.listofitems.fake.FakeDataSource
import com.example.listofitems.fake.FakeNetworkItemsRepository
import com.example.listofitems.rules.TestDispatcherRule
import com.example.listofitems.ui.ItemUiState
import com.example.listofitems.ui.ItemsViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ItemsViewModelUiStateTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun itemsViewModel_getItems_verifyUiStateSuccess()  =
        runTest {
            val viewModel = ItemsViewModel(FakeNetworkItemsRepository())

            val uiState = viewModel.uiState.value
            assertEquals(Result.Success(FakeDataSource.ItemList), (uiState as ItemUiState.HasItems).items)
        }

}