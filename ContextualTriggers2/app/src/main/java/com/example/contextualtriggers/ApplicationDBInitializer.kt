package com.example.contextualtriggers

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.contextualtriggers.data.ApplicationDatabase
import com.example.contextualtriggers.data.dao.ActivityDao
import com.example.contextualtriggers.data.dao.StepsDao
import com.example.contextualtriggers.data.repository.ActivityRepository
import com.example.contextualtriggers.data.repository.StepsCounterRepository

object ApplicationDBInitializer {

    private lateinit var database: ApplicationDatabase
    private var stepsDao: StepsDao? = null
    private var activityDao: ActivityDao? = null

    val stepCounterRepository: StepsCounterRepository
        get() = StepsCounterRepository(stepsDao!!)

    val activityRepository: ActivityRepository
    get() = ActivityRepository(activityDao!!)



    fun provide(context: Context) {
        Log.d("ApplicationDBInitializer", "ApplicationDBInitializer")
        database = Room.databaseBuilder(
            context.applicationContext,
            ApplicationDatabase::class.java,
            "contextualTriggers"
        ).fallbackToDestructiveMigration().build()
        stepsDao = database.stepsDao()
        activityDao=database.activityDao()


    }


    fun getDatabase(): ApplicationDatabase {
        return database
    }
}
