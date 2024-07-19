package com.example.listofitems

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.listofitems.data.NetWorkItemsRepository
import com.example.listofitems.fake.FakeItemApiService
import com.example.listofitems.theme.ListOfItemsTheme
import com.example.listofitems.ui.ItemsScreen
import com.example.listofitems.ui.ItemsViewModel
import kotlinx.coroutines.test.runTest
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

    // test the Context of the app
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.listofitems", appContext.packageName)
    }

    // UI test
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLazyColumnExistsAfterDataFetch() =
        runTest {
            val viewModel = ItemsViewModel(NetWorkItemsRepository(FakeItemApiService()))
            composeTestRule.setContent {
                ItemsScreen()
            }
            viewModel.getItems()
            composeTestRule.awaitIdle()
            composeTestRule.onNodeWithText("Name").assertIsDisplayed()
        }

//    @Test
//    fun testRowHasTwoText() {
//        composeTestRule.setContent {
//            ListOfItemsTheme {
//                ItemsScreen()
//            }
//        }
//    }
}
