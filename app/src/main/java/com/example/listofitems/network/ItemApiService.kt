package com.example.listofitems.network

import com.example.listofitems.model.Item
import retrofit2.Response
import retrofit2.http.GET

interface ItemApiService {
    @GET("hiring.json")
    // using suspend function because retrofit offers main-safe call when using coroutines
    suspend fun getItems(): Response<List<Item>>
}