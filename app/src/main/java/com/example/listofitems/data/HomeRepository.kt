package com.example.listofitems.data


import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.example.listofitems.model.Item
import com.example.listofitems.network.HomeApiService
import java.lang.IllegalArgumentException


interface HomeRepository {
    // this function will be used to fetch the data
    suspend fun getItems(): Result<List<Item>>
}


class DefaultHomeRepository(private val apiService : HomeApiService) : HomeRepository {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getItems(): Result<List<Item>> {
        // response return a value create asynchronously from the apiService
        val response = apiService.getItems()
        // check if the response is successful
        return try {
            if(response.isSuccessful) {
                // if successful, return the body
                Result.Success(response.body()!!)
            } else {
                Log.d("error", "errorGetItems: ${retrofit2.HttpException(response)}")
                // if not successful, return an error
                Result.Error(retrofit2.HttpException(response))
            }
        } catch(exception: Exception) {
            Log.d("exception", "exceptionGetItems: $exception")
            Result.Error(exception)
        }
    }
}