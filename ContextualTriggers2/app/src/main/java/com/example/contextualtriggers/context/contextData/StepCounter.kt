package com.example.contextualtriggers.context.contextData

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.example.contextualtriggers.context.ContextManager
import java.util.*


class StepCounter: Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var stepDetectorSensor: Sensor
    private var stepCount: Int = 0

    companion object {
        private const val TAG = "StepCounter"
    }
    private  var lastStepCount = 0
    private var lastBootTime: Long = 0
    private var contextHolder: ContextManager? = null




    override fun onCreate() {
        super.onCreate()
        val notificationTitle = "StepCounter ForegroundService"
        val notificationMessage = "StepCounter ForegroundService is running"
        val notificationId = 1
        ForeGroundServiveNotification.startForegroundService(
            this, notificationTitle, notificationMessage,
            notificationId, this
        )


        contextHolder = ContextManager(this)

        sensorManager=getSystemService(SENSOR_SERVICE) as SensorManager
        stepDetectorSensor= sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        lastBootTime = System.currentTimeMillis() - SystemClock.elapsedRealtime()
        Log.d("stepDetector  lastBootTime","$lastBootTime")

        val sharedPreferences = getSharedPreferences("step_count", Context.MODE_PRIVATE)
        stepCount = sharedPreferences.getInt(getTodayDateString(), 0)

        if (stepDetectorSensor == null) {
            Log.e(TAG, "Step count sensor not available")
        } else {
            Log.i(TAG, "Step count sensor available")
            sensorManager?.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }
    override fun onSensorChanged(event: SensorEvent?) {
      //  contextHolder=ContextManager(this)
        val handler = Handler(Looper.getMainLooper())
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val newStepCount = event.values[0].toInt()
            Log.d(TAG, "Step count: $newStepCount")
            if (lastStepCount == 0) {
                lastStepCount = newStepCount
            }
            stepCount += newStepCount - lastStepCount
            lastStepCount = newStepCount
            Toast.makeText(this, "Step Detecting Start $stepCount", Toast.LENGTH_SHORT).show()
            val runnable = object : Runnable {
                override fun run() {

                    handler.postDelayed(this, 15000) // Schedule the task to run again in 5 minutes (300 seconds)
                }
            }

           if (isStartOfNewDay(this )) {
               Log.d("isStartOfNewDay","I am inside isStartOfNewDay")
               stepCount = 0
               saveStepsToday()

            }
            else
           {
               Log.d("isStartOfNewDay","isStartOfNewDay")
               saveStepsToday()
           }


            Log.d(TAG, "Step count: $stepCount")
        }
    }


    private fun isStartOfNewDay(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var lastDayChecked = sharedPreferences.getInt("lastDayChecked", -1)

        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_YEAR)

        val dayChanged = currentDay != lastDayChecked

        if (dayChanged) {
            lastDayChecked = currentDay
            sharedPreferences.edit().putInt("lastDayChecked", lastDayChecked).apply()
            Log.d("last day checked", "$lastDayChecked")
        }

        Log.d("day changed", "$dayChanged")
        Log.d("current day", "$currentDay")

        return dayChanged
    }



    private fun getTodayDateString(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val finaldate = String.format("%04d%02d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
        return finaldate
    }

    private fun saveStepsToday() {
        val sharedPreferences = getSharedPreferences("step_count", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(getTodayDateString(), stepCount).apply()
        Log.d("saveStepsTodaysssssssss","$stepCount")
        contextHolder?.addSteps(stepCount)
        Log.d("saveStepsToday", "$stepCount")
    }


}