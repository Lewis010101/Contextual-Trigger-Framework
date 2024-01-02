package com.example.contextualtriggers.trigger

import android.content.Intent
import com.example.contextualtriggers.context.ContextHolder


class BatteryTrigger(holder: ContextHolder) : Trigger {
    private val mContextHolder: ContextHolder
    private val notificationTitle: String
    private val notificationMessage: String
    private val mThreshold: Int
    private val triggerPriority:Int

    init {
        mContextHolder = holder
        mThreshold = 30
        notificationTitle = "Battery Notification"
        notificationMessage = "You have a lot of battery left, you can go for a walk."
        triggerPriority = 7
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
        val batteryLevel = mContextHolder.getBatteryLevel()

        return batteryLevel >= mThreshold && batteryLevel != 1
    }

    override fun getPriority():Int{
        return triggerPriority
    }

}