package com.example.listofitems.network

import com.example.listofitems.model.Item
import retrofit2.Response
import retrofit2.http.GET

interface ItemApiService {
    @GET("hiring.json")
    suspend fun getItems(): Response<List<Item>>
}