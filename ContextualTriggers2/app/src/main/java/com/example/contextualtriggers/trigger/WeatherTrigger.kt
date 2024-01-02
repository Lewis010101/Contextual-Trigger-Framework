package com.example.contextualtriggers.trigger

import android.content.Intent
import android.util.Log
import com.example.contextualtriggers.context.ContextHolder

class WeatherTrigger(holder: ContextHolder) : Trigger {
    private val mContextHolder: ContextHolder
    private val notificationTitle: String
    private val notificationMessage: String
    private val triggerPriority:Int

    init {
        mContextHolder = holder
        notificationTitle = "Weather Notification"
        notificationMessage = "It's nice weather outside go for a walk."
        triggerPriority = 6
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

        val weatherMain = mContextHolder.getWeather()
        Log.d("Current weather main:", "$weatherMain")


        return weatherMain == "Clouds" || weatherMain == "Clear"


    }

    override fun getPriority():Int{
        return triggerPriority
    }

}