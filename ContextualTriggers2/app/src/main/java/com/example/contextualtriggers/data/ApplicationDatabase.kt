package com.example.contextualtriggers.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.contextualtriggers.data.dao.ActivityDao
import com.example.contextualtriggers.data.dao.StepsDao
import com.example.contextualtriggers.data.entity.ActivityTable
import com.example.contextualtriggers.data.entity.StepsCount


@Database(
entities =[StepsCount::class,ActivityTable::class],
version = 6,
exportSchema =false


)

abstract class ApplicationDatabase: RoomDatabase() {
    abstract fun stepsDao(): StepsDao
    abstract fun activityDao(): ActivityDao

    }









