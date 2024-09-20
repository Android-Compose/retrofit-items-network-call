package com.example.listofitems.data

import com.example.listofitems.model.Item
import com.example.listofitems.network.ItemApiService
import retrofit2.HttpException
import java.io.IOException


interface ItemsRepository {
    // this function will be used to fetch the data
    suspend fun getItems(): Result<List<Item>>
}

/**
 * network implementation of repository that fetch data from the api
 */

class NetWorkItemsRepository(private val apiService : ItemApiService) : ItemsRepository {
    override suspend fun getItems(): Result<List<Item>> {
        // Make network request using a blocking call from suspend function from retrofit
        return try {
            // response return a value create asynchronously from the apiService
            val response = apiService.getItems()
            // if successful, return the body
            if(response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                // if not successful, return a response error
                Result.Error(HttpException(response))
            }
        } catch(io : IOException) {
            Result.Error(io) // returns network error
        }
    }
}