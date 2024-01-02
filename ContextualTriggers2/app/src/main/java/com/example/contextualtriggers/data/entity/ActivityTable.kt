package com.example.contextualtriggers.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "activityTable"
)

data class ActivityTable(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name= "id") val id :Int = 1,
    @ColumnInfo(name="activityType") val activityType :Int =0,
    @ColumnInfo(name="description") val description :String
) {
}