package com.example.contextualtriggers.trigger

import android.content.Intent
import android.util.Log

class WeatherTemperatureTrigger(private val triggers: List<Trigger>) : Trigger {

    private val notificationTitle: String
    private val notificationMessage: String

    init {
        notificationTitle = "Weather and Temp Trigger"
        notificationMessage = "It's nice weather and warm outside. Your battery is charged why not go for a walk."
    }

    override fun getNotificationTitle(): String {
        return  notificationTitle
    }

    override fun getNotificationMessage(): String {
        return notificationMessage
    }

    override fun getNotificationIntent(): Intent? {
        return null
    }

    override fun isTriggered(): Boolean {
        for (trigger in triggers) {
            if (!trigger.isTriggered()) {
                return false
            }
        }
        return true
    }

    override fun getPriority(): Int {
        var combinedPriority = 0
        for (trigger in triggers) {
            combinedPriority += trigger.getPriority()
        }
        Log.d("Current priority", "$combinedPriority")
        return combinedPriority
    }
}