package com.example.contextualtriggers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.Worker
import androidx.work.WorkerParameters

import com.example.contextualtriggers.context.ContextManager
import com.example.contextualtriggers.context.contextData.*
import com.example.contextualtriggers.trigger.TriggerManager

class CTWorker (context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val contextHolder = ContextManager(context)
    private val triggerManager = TriggerManager(context, contextHolder)

    @SuppressLint("SuspiciousIndentation")
    override fun doWork(): Result {

      val stepCounter = Intent(applicationContext, StepCounter::class.java)

      val activityRecognition = Intent(applicationContext, ActivityRecognitionService::class.java)

        triggerManager.handleNotification()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            applicationContext.startForegroundService(stepCounter)
            applicationContext.startForegroundService(activityRecognition)


        }
else {
            applicationContext.startService(stepCounter)
            applicationContext.startService(activityRecognition)


            }

        return Result.success()
    }
}