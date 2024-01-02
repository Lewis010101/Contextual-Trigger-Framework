package com.example.contextualtriggers.trigger

import android.content.Intent
import com.example.contextualtriggers.context.ContextHolder

class CalendarTrigger(holder: ContextHolder) : Trigger {
    private val mContextHolder: ContextHolder
    private val notificationTitle: String
    private val notificationMessage: String
    private val triggerPriority:Int

    init {
        mContextHolder = holder
        notificationTitle = "Calendar Notification"
        notificationMessage = "Your calendar is empty go for a walk."
        triggerPriority = 4
    }

    override fun getNotificationTitle(): String {
        return notificationTitle
    }

    override fun getNotificationMessage(): String {
        return notificationMessage
    }

    override fun getNotificationIntent(): Intent? {
        return null
    }

    override fun isTriggered(): Boolean {
        val calendarBusy = mContextHolder.getIsCalendarEmpty()

        return calendarBusy == "true"
    }

    override fun getPriority():Int{
        return triggerPriority
    }

}