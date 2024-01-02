package com.example.contextualtriggers.context.contextData

import android.annotation.SuppressLint

import android.app.PendingIntent
import android.app.Service
import android.content.Intent

import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.contextualtriggers.BuildConfig
import com.example.contextualtriggers.context.ContextManager
import com.google.android.gms.common.internal.safeparcel.SafeParcelableSerializer
import com.google.android.gms.location.*


class ActivityRecognitionService :   Service() {



    private lateinit var activityRecognitionClient: ActivityRecognitionClient
    private lateinit var activityTransitionPendingIntent: PendingIntent
    private var contextHolder: ContextManager? = null

    override fun onCreate() {
        super.onCreate()
        val notificationTitle = "Activity ForegroundService"
        val notificationMessage = "Activity ForegroundService is running"
        val notificationId = 2
        ForeGroundServiveNotification.startForegroundService(
            this, notificationTitle, notificationMessage,
            notificationId, this
        )

        contextHolder = ContextManager(this)


        activityRecognitionClient = ActivityRecognition.getClient(this)

        activityTransitionPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, ActivityRecognitionBroadcastReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ActivityRecognitionService", "onStartCommand")
        requestActivityUpdates()
        return START_STICKY
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestActivityUpdates() {
        val request = ActivityTransitionsUtil.getActivityTransitionRequest()
        val intent = Intent(this, ActivityRecognitionBroadcastReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("pendingIntent","$pendingIntent")

        val task = activityRecognitionClient.requestActivityTransitionUpdates(request, pendingIntent)
        task.addOnSuccessListener {

            val intent = Intent(this, ActivityRecognitionBroadcastReceiver::class.java)


            intent.action = BuildConfig.APPLICATION_ID + "TRANSITIONS_RECEIVER_ACTION"
           var events: ArrayList<ActivityTransitionEvent> = arrayListOf()



         var transitionEvent = ActivityTransitionEvent(DetectedActivity.STILL, ActivityTransition.ACTIVITY_TRANSITION_ENTER, SystemClock.elapsedRealtimeNanos())
            events.add(transitionEvent)
            var result = ActivityTransitionResult(events)
            SafeParcelableSerializer.serializeToIntentExtra(result, intent, "com.google.android.location.internal.EXTRA_ACTIVITY_TRANSITION_RESULT")
           this ?.sendBroadcast(intent)
            Toast.makeText(this, "Activity updates requested", Toast.LENGTH_SHORT).show()
        }
        task.addOnFailureListener { e ->
            Toast.makeText(this, "Failed to request activity updates: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(this, ActivityRecognitionBroadcastReceiver::class.java)
        Log.d("intent","$intent")

        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }



    override fun onDestroy() {
        super.onDestroy()

    }


}