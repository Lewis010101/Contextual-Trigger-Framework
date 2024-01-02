package com.example.contextualtriggers.trigger

import android.content.Intent
import com.example.contextualtriggers.context.ContextHolder


class TimeTrigger(holder: ContextHolder) : Trigger {
    private val mContextHolder: ContextHolder
    private val notificationTitle: String
    private val notificationMessage: String
    private val triggerPriority:Int

    init {
        mContextHolder = holder
        notificationTitle = "Time Notification"
        notificationMessage = "Its morning go for a walk."
        triggerPriority = 3
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
        val currentTime = mContextHolder.getCurrentTime()

        val time0700Int = 700
        val time0900Int = 900
        return currentTime in time0700Int..time0900Int



    }

    override fun getPriority():Int{
        return triggerPriority
    }

}