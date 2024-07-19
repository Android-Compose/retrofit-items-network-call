package com.example.listofitems.fake

import com.example.listofitems.model.Item
import com.example.listofitems.network.ItemApiService
import retrofit2.Response


class FakeItemApiService : ItemApiService {
    override suspend fun getItems(): Response<List<Item>> = Response.success(FakeDataSource.ItemList)
}