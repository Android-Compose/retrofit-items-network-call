package com.example.listofitems


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.listofitems.fake.FakeNetworkItemsRepository
import com.example.listofitems.ui.ItemUiState
import com.example.listofitems.ui.ItemsScreen
import com.example.listofitems.ui.ItemsViewModel
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ItemsInstrumentedTest {

    // UI test
    @get:Rule
    val composeTestRule = createComposeRule()

    // test the Context of the app
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.listofitems", appContext.packageName)
    }

    @Test
    fun testTwoTextExistsInRow() {
        val viewModel = ItemsViewModel(repository = FakeNetworkItemsRepository())

        composeTestRule.setContent {
            ItemsScreen()
        }

        val uiState = viewModel.uiState.value
        check(uiState is ItemUiState.HasItems)

        composeTestRule.waitUntil(timeoutMillis = 10000) {
            uiState.items.isNotEmpty()
        }

        composeTestRule.onRoot().printToLog("TAG")

        composeTestRule.onNodeWithText("ListId").assertIsDisplayed()
        composeTestRule.onNodeWithText("Name").assertIsDisplayed()

    }
}
