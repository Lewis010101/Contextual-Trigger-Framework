package com.example.contextualtriggers


import android.app.Application

import androidx.activity.result.ActivityResultLauncher

import androidx.room.Room
import com.example.contextualtriggers.data.ApplicationDatabase

class ContextualTriggers:Application() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    lateinit var db: ApplicationDatabase
    private var isActivityRecognitionPermisionGranted = false
    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            ApplicationDatabase::class.java, "contextualTriggers"
        ).build()

    }
}