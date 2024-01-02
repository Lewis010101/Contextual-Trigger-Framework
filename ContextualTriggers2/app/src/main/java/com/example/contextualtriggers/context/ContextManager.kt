package com.example.contextualtriggers.context

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

import android.util.Log

import com.example.contextualtriggers.ApplicationDBInitializer
import com.example.contextualtriggers.ApplicationDBInitializer.stepCounterRepository
import com.example.contextualtriggers.context.contextData.*


import com.example.contextualtriggers.data.entity.StepsCount
import com.example.contextualtriggers.data.repository.ActivityRepository

import com.example.contextualtriggers.data.repository.StepsCounterRepository



import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture
import kotlin.random.Random


class ContextManager (private val context: Context) : ContextHolder {

    private var mContext: Context? = null

    private lateinit var stepsCounterRepository: StepsCounterRepository
    private lateinit var activityRepository: ActivityRepository
    private val today = LocalDateTime.now()

    init {
        Log.d("Init of context Manager", "Initialize DB")

        mContext = context
        ApplicationDBInitializer.provide(mContext!!)
        val stepsDao = ApplicationDBInitializer.getDatabase().stepsDao()
        stepsCounterRepository = StepsCounterRepository(stepsDao)
        val activityDao = ApplicationDBInitializer.getDatabase().activityDao()
        activityRepository = ActivityRepository(activityDao)



    }

    override fun getBatteryLevel(): Int {
        Log.d("ContextManager", "getBatteryLevel")
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = mContext?.registerReceiver(null, ifilter)

        return if (batteryStatus != null) {
            val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

            val batteryPct = (level / scale.toFloat()) * 100
            Log.d("BATTERY LEVEL", "$batteryPct")
            println("BATTERY LEVEL: $batteryPct")
            batteryPct.toInt()
        } else {
            -1
        }
    }


    fun addSteps(steps: Int) {


        val today = LocalDateTime.now()
        val date = today.dayOfMonth
        val month = today.monthValue
        val year = today.year

        var existingStepsCount: StepsCount?

        Thread {
            existingStepsCount = stepCounterRepository.getStepsCount()

            Log.d("existingStepsCount", "$existingStepsCount")

            if (existingStepsCount != null) {
                Log.d("existingStepsCount", "$existingStepsCount")
                val updatedStepsCount = StepsCount(
                    id = existingStepsCount!!.id,
                    stepsCount = steps,
                    date = existingStepsCount!!.date,
                    month = existingStepsCount!!.month,
                    year = existingStepsCount!!.year
                )
                stepsCounterRepository.update(updatedStepsCount)




            } else {
                Log.d("Else add step", "I am in else part")

                val stepsCount = StepsCount(
                    id = Random.nextInt(1, Int.MAX_VALUE),
                    stepsCount = steps,
                    date = date,
                    month = month,
                    year = year
                )

                stepCounterRepository.insert(stepsCount)
                Log.d("Insert Steps ", "$stepsCount")
            }
        }.start()
    }


    override fun sendSteps(): CompletableFuture<Int> {
        return CompletableFuture.supplyAsync {
            stepsCounterRepository.getStepsCount()?.stepsCount ?: 0
        }
    }


    override fun getWeather(): String {
        val apiKey = "211aa4447dfa3718a7817c2e2d89d639"
        val latitude = 55.836212 // call fuse location latitude
        val longitude = -4.515706 // call fuse location longitude

        var weatherData: String? = null
        val lock = Object()
        getCurrentWeather(apiKey, latitude, longitude) { weather ->
            weatherData = weather.toString()
            synchronized(lock) {
                lock.notify()
            }
        }
        synchronized(lock) {
            lock.wait()
        }

        return weatherData ?: "unknown"
    }

    override fun getTemperature(): Double {
        val apiKey = "211aa4447dfa3718a7817c2e2d89d639"
        val latitude = 55.836212 // call fuse location latitude
        val longitude = -4.515706 // call fuse location longitude

        var weatherData: Double? = 0.0
        val lock = Object()
        getCurrentTemperature(apiKey, latitude, longitude) { weather ->
            weatherData = weather.toDouble()
            synchronized(lock) {
                lock.notify()
            }
        }
        synchronized(lock) {
            lock.wait()
        }

        return weatherData ?: 0.0
    }




    override fun getDetectedActivity(): CompletableFuture<String> {
        Log.d("getDetectedActivity","inside ContextHolder getDetectedActivity")

        return CompletableFuture.supplyAsync {
            Log.d("activityRepository", "This works")
            activityRepository.getActivity()

        }



    }
    override fun getIsCalendarEmpty(): String {
        var eventData: String? = null
        val lock = Object()
        getEventsForToday(context) { eventCheck ->
            eventData = eventCheck.toString()
            synchronized(lock) {
                lock.notify()
            }
        }
        synchronized(lock) {
            lock.wait()
        }

        return eventData ?: "unknown"
    }

    override fun getCurrentTime(): Int {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HHmm")
        val formattedTime = currentTime.format(formatter)
        Log.d("formattedTime","$formattedTime")
        return formattedTime.toInt()

    }



}






