package com.example.contextualtriggers.data.repository


import android.util.Log
import com.example.contextualtriggers.ApplicationDBInitializer
import com.example.contextualtriggers.data.dao.ActivityDao
import com.example.contextualtriggers.data.entity.ActivityTable
import com.example.contextualtriggers.data.entity.StepsCount
import java.time.LocalDate


class ActivityRepository(
    private val activityDao: ActivityDao = ApplicationDBInitializer.getDatabase().activityDao())
{
    fun insert(activityTable: ActivityTable): Long
    {
        return activityDao.insert(activityTable)

    }

    fun getActivity(): String {
        return activityDao.getActivity()
    }

}