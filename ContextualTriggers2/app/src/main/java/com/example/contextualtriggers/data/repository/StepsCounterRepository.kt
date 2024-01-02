package com.example.contextualtriggers.data.repository

import android.util.Log
import com.example.contextualtriggers.ApplicationDBInitializer
import com.example.contextualtriggers.data.dao.StepsDao
import com.example.contextualtriggers.data.entity.StepsCount
import java.time.LocalDate

class StepsCounterRepository(
private val stepsDao: StepsDao = ApplicationDBInitializer.getDatabase().stepsDao()
) {

    fun insert(stepsCount: StepsCount): Long
    {
        return stepsDao.insert(stepsCount)

    }

    fun updateStepsCount(id:Int,stepsCount: Int,date: Int, month: Int, year: Int, ) {
        Log.d("Update Step Count","$date $month $year $stepsCount")
        return stepsDao.updateStepsCount(id,stepsCount,date, month, year)
    }

    fun getStepsCount(): StepsCount {
        val today= LocalDate.now()
        val date =today.dayOfMonth
        val month = today.monthValue
        val year = today.year
        Log.d("Date","$date")
        return stepsDao.getStepsCount(date, month, year)
    }
    fun update(stepsCount: StepsCount)
    {
        return stepsDao.update(stepsCount)
    }

}