package com.example.listofitems

import android.app.Application
import com.example.listofitems.data.AppContainer
import com.example.listofitems.data.DefaultAppContainer

class HomeApplication : Application() {
    // AppContainer instance used by the rest of the app to obtain the dependencies
    private lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}