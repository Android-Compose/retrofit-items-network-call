package com.example.listofitems

import android.app.Application
import com.example.listofitems.data.AppContainer
import com.example.listofitems.data.DefaultAppContainer

class ItemsApplication : Application() {
    // AppContainer instance used by the rest of the app to obtain the dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}