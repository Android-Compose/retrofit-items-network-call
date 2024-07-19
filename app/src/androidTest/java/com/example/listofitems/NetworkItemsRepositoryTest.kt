package com.example.listofitems



import com.example.listofitems.data.NetWorkItemsRepository
import com.example.listofitems.data.Result
import com.example.listofitems.fake.FakeDataSource
import com.example.listofitems.fake.FakeItemApiService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test


/**
 * This Unit tests ensure that the data is fetched from the network and does not
 * depends on untested api that could cause problems over the time.
 */

class NetworkItemsRepositoryTest {

    @Test
    fun networkItemsRepository_GetItems_ReturnsSuccess() = runTest {
        val repository = NetWorkItemsRepository(apiService = FakeItemApiService())
        assertEquals(Result.Success(FakeDataSource.ItemList), repository.getItems())
    }
}