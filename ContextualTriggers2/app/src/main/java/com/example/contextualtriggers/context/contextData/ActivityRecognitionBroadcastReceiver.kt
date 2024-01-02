package com.example.contextualtriggers.context.contextData

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log

import com.example.contextualtriggers.ApplicationDBInitializer
import com.example.contextualtriggers.ApplicationDBInitializer.activityRepository
import com.example.contextualtriggers.context.contextData.ActivityTransitionsUtil.toActivityString
import com.example.contextualtriggers.data.entity.ActivityTable
import com.example.contextualtriggers.data.repository.ActivityRepository

import com.google.android.gms.location.ActivityTransitionResult

import java.util.concurrent.CompletableFuture


class ActivityRecognitionBroadcastReceiver() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ActivityRecognitionBroadcastReceiver", "onReceive Intent")
         lateinit var activityRepository: ActivityRepository
        ApplicationDBInitializer.provide(context)

        val activityDao = ApplicationDBInitializer.getDatabase().activityDao()
        activityRepository = ActivityRepository(activityDao)



        val results = ActivityTransitionResult.hasResult(intent)

        val handler = Handler(Looper.getMainLooper())
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)
            var transitionEvent: Int = 4
            var description: String =""

            if (result != null) {
                result.transitionEvents.forEach { event ->

                    transitionEvent = event.activityType
                    description=toActivityString(transitionEvent)

                    saveActivityToDB(transitionEvent,description)
                    Log.d("transitionEvent","$transitionEvent   $description")


                }
            }


        }
    }
    private fun saveActivityToDB(activityType: Int, activityDescription: String) {
        Log.d("saveActivityToDB", "Inside saveActivityToDB")

        val activityTable = ActivityTable(
            activityType = activityType,
            description = activityDescription
        )

        CompletableFuture.supplyAsync {
            activityRepository.insert(activityTable)
        }


    }


}
