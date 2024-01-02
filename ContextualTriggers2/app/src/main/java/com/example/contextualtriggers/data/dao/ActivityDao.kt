package com.example.contextualtriggers.data.dao


import androidx.room.*
import com.example.contextualtriggers.data.entity.ActivityTable



@Dao

abstract class ActivityDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)

    abstract fun insert(activityTable: ActivityTable): Long


    @Query("SELECT description FROM activityTable ORDER BY id DESC LIMIT 1 ")
    abstract fun getActivity(): String



}