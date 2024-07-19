package com.example.listofitems.data

import android.util.Log
import com.example.listofitems.model.Item
import com.example.listofitems.network.ItemApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


interface ItemsRepository {
    // this function will be used to fetch the data
    suspend fun getItems(): Result<List<Item>>
}


// network implementation of repository that fetch data from the api
class NetWorkItemsRepository(private val apiService : ItemApiService) : ItemsRepository {
    override suspend fun getItems(): Result<List<Item>> {
        // check if the response is successful
        return withContext(Dispatchers.IO) {
            try {
                // response return a value create asynchronously from the apiService
                val response = apiService.getItems()
                // if successful, return the body
                if(response.isSuccessful) {
                    Result.Success(response.body() ?: emptyList())
                } else {
                    Log.d("httpException", "errorGetItems: ${HttpException(response)}")
                    // if not successful, return an error
                    Result.Error(HttpException(response))
                }
            } catch(io : IOException) {
                Log.d("ioException", "IoExceptionGetItems: $io")
                Result.Error(io)
            } catch(exception: Exception) {
                Log.d("exception", "exceptionGetItems: $exception")
                Result.Error(exception)
            }
        }
    }
}