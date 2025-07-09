package com.example.newapplication

import android.app.Application
import com.cloudinary.android.MediaManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()


            val config: HashMap<String, String> = HashMap()
            config["cloud_name"] = "djsbyqbek"
            MediaManager.init(this, config)

    }
}