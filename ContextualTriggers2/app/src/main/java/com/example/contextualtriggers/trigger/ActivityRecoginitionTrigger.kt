package com.example.contextualtriggers.trigger


import android.content.Intent
import android.util.Log
import com.example.contextualtriggers.context.ContextHolder


class ActivityRecoginitionTrigger(holder: ContextHolder) : Trigger {
    private val mContextHolder: ContextHolder
    private val notificationTitle: String
    private val notificationMessage: String
    private val mThreshold: Int
    private var detectedActivities :String = ""
    private val triggerPriority:Int


    init {
        mContextHolder = holder
        mThreshold = 30
        notificationTitle = "Activity Notification"
        val future = mContextHolder.getDetectedActivity()
        future.thenAccept {
            detectedActivities = it
            Log.d("detectedActivities Trigger", "$detectedActivities")
        }
        Log.d("Trigger detectedActivities", "$detectedActivities")
        triggerPriority = 1
        val value= detectedActivities

        notificationMessage = "Activity Triggered "

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


        return detectedActivities == "STILL"
    }
    override fun getPriority():Int{
        return triggerPriority
    }
}
