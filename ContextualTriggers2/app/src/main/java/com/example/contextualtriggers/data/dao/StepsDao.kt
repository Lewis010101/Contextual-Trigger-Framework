package com.example.contextualtriggers.data.dao

import androidx.room.*
import com.example.contextualtriggers.data.entity.StepsCount

@Dao

abstract class StepsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)

    abstract fun insert(stepsCount: StepsCount): Long


    @Query("SELECT * FROM stepsCount WHERE date = :date AND month = :month AND year = :year")
    abstract fun getStepsCount(date: Int, month: Int, year: Int): StepsCount

    @Query("UPDATE stepsCount SET stepsCount = :steps , date = :date , month = :month , year = :year WHERE id=:id" )
    abstract fun updateStepsCount(id:Int,steps: Int, date: Int, month: Int, year: Int)



    @Update
    abstract fun update(entity: StepsCount)
    /*@Query("Select stepsCount FROM stepsCount where date =:date")
    abstract fun getStepsCountforDate(date :Int):Int*/



}