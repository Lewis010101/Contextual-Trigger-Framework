package com.example.contextualtriggers.data.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(
    tableName = "stepsCount"
)

data class StepsCount(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name= "id") val id :Int ,
    @ColumnInfo(name="stepsCount") val stepsCount :Int =0,
    @ColumnInfo(name="date") val date : Int,
    @ColumnInfo(name="month") val month : Int,
    @ColumnInfo(name="year") val year : Int
) {
}