package com.example.listofitems.data

import com.example.listofitems.BASE_URL
import com.example.listofitems.network.HomeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val repository: HomeRepository
}


class DefaultAppContainer : AppContainer {
    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: HomeApiService by lazy {
        retrofit.create(HomeApiService::class.java)

    }
    // Data injection for home Repository
    override val repository: HomeRepository
        get() = DefaultHomeRepository(retrofitService)
}