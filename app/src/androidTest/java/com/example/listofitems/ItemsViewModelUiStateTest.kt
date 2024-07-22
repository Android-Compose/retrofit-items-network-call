package com.example.listofitems


import com.example.listofitems.fake.FakeDataSource
import com.example.listofitems.fake.FakeNetworkItemsRepository
import com.example.listofitems.rules.TestDispatcherRule
import com.example.listofitems.ui.ItemUiState
import com.example.listofitems.ui.ItemsViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ItemsViewModelUiStateTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun itemsViewModel_handling_success()  =
        runTest {
            val viewModel = ItemsViewModel(repository = FakeNetworkItemsRepository())

            val uiState = viewModel.uiState.value
            println("viewModel" + viewModel.uiState)
            println("uiStateItems" + "${(uiState as ItemUiState.HasItems).items}")
            assertEquals(FakeDataSource.itemList, uiState.items)
            assertFalse(uiState.loading)
            assertEquals("", uiState.errorMessage)
        }
    @Test
    fun itemsViewModel_handling_error() =
        runTest {
            val viewModel = ItemsViewModel(repository = FakeNetworkItemsRepository())

            val uiState = viewModel.uiState.value
            val exception = Exception("Failed to load data")
            assertEquals("Network error", uiState.errorMessage) // Check error message
        }
}