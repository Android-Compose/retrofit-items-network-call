package com.example.listofitems.data

import com.example.listofitems.model.Item
import com.example.listofitems.network.ItemApiService
import retrofit2.HttpException
import java.io.IOException


interface ItemsRepository {
    suspend fun getItems(): Result<List<Item>> // this function will fetch the data
}

/**
 * network implementation of repository that fetch data from the api
 */
class NetWorkItemsRepository(private val apiService : ItemApiService) : ItemsRepository {
    override suspend fun getItems(): Result<List<Item>> {
        // Make network request using a blocking call of the suspend function from retrofit
        return try {
            // response returns data create asynchronously from the apiService
            val response = apiService.getItems()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList()) //return the data
            } else {
                Result.Error(HttpException(response)) //return error
            }

        } catch (io: IOException){
            Result.Error(io) // network connectivity error
        } catch (e: Exception){
            Result.Error(e) // returns generic error
        }
    }
}

//class NetWorkItemsRepository(private val apiService : ItemApiService) : ItemsRepository {
//    override suspend fun getItems(): Result<List<Item>> {
//        // Make network request using a blocking call from suspend function from retrofit
//        return try {
//            // response return a value create asynchronously from the apiService
//            val response = apiService.getItems()
//            // if successful, return the body
//            if(response.isSuccessful) {
//                Result.Success(response.body() ?: emptyList())
//            } else {
//                // if not successful, return a response error
//                Result.Error(HttpException(response))
//            }
//        } catch(io : IOException) {
//            Result.Error(io) // returns network error
//        }
//    }
//}