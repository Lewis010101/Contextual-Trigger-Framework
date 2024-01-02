package com.example.contextualtriggers.trigger

import android.content.Intent
import android.util.Log
import com.example.contextualtriggers.context.ContextHolder

class StepCounterTrigger(holder: ContextHolder) : Trigger {
    private val mContextHolder: ContextHolder
    private val notificationTitle: String
    private val notificationMessage: String
    private val triggerPriority:Int
    private var stepCount:Int = 0

    init {
        mContextHolder = holder
        val future = mContextHolder.sendSteps()
        future.thenAccept {
            stepCount = it
            Log.d("stepCount", "$stepCount")
        }
        Log.d("stepCount", "$stepCount")
        notificationTitle = "StepCounter Notification"
        notificationMessage = "You have not walked enough steps today"
        triggerPriority = 2
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

        return stepCount<2500

    }

    override fun getPriority():Int{
        return triggerPriority
    }
}