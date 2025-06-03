package com.example.listofitems


import android.util.Log
import com.example.listofitems.fake.FakeDataSource
import com.example.listofitems.fake.FakeNetworkItemsRepository
import com.example.listofitems.model.Item
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
            check(uiState is ItemUiState.HasItems)
            val listOfItems: MutableList<Item> = mutableListOf()
            for( entry in  uiState.items) {
               entry.value.forEach{ listOfItems.add(it) }
            }
            assertEquals(FakeDataSource.itemList, listOfItems)
            assertFalse(uiState.loading)
            assertEquals("", uiState.errorMessage)
        }
}