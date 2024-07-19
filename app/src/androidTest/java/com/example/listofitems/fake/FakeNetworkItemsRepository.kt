package com.example.listofitems.fake


import com.example.listofitems.data.ItemsRepository
import com.example.listofitems.data.Result
import com.example.listofitems.model.Item

class FakeNetworkItemsRepository : ItemsRepository {
    override suspend fun getItems(): Result<List<Item>> = Result.Success(FakeDataSource.ItemList)
}
