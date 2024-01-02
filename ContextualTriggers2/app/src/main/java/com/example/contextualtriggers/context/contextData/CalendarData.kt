package com.example.contextualtriggers.context.contextData


import android.content.ContentResolver
import android.content.Context
import android.provider.CalendarContract
import java.util.*



    fun getEventsForToday(context: Context, callback: (String) -> Unit) {
        Thread {
            val calendar = Calendar.getInstance()
            val startOfDayMillis = calendar.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val endOfDayMillis = calendar.apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }.timeInMillis

            val events = mutableListOf<Event>()

            val contentResolver: ContentResolver = context.contentResolver
            val uri = CalendarContract.Events.CONTENT_URI
            val projection = arrayOf(
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND
            )
            val selection =
                "${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTEND} <= ?"
            val selectionArgs = arrayOf(startOfDayMillis.toString(), endOfDayMillis.toString())
            val sortOrder = "${CalendarContract.Events.DTSTART} ASC"

            val cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
            cursor?.use {
                val idIndex = it.getColumnIndex(CalendarContract.Events._ID)
                val titleIndex = it.getColumnIndex(CalendarContract.Events.TITLE)
                val startMillisIndex = it.getColumnIndex(CalendarContract.Events.DTSTART)
                val endMillisIndex = it.getColumnIndex(CalendarContract.Events.DTEND)

                while (it.moveToNext()) {
                    val id = if (idIndex != -1) it.getLong(idIndex) else -1
                    val title = if (titleIndex != -1) it.getString(titleIndex) else ""
                    val startMillis =
                        if (startMillisIndex != -1) it.getLong(startMillisIndex) else -1
                    val endMillis = if (endMillisIndex != -1) it.getLong(endMillisIndex) else -1

                    events.add(Event(id, title, startMillis, endMillis))
                }
            }
            val eventCheck: String
            eventCheck = if (events.isEmpty()) {
                "true"
            } else {
                "false"
            }

            callback(eventCheck)
        }.start()
    }


data class Event(
    val id: Long,
    val title: String,
    val startMillis: Long,
    val endMillis: Long
)