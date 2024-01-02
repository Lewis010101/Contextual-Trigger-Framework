package com.example.contextualtriggers.trigger

import android.content.Context
import android.util.Log
import com.example.contextualtriggers.NotificationManager.NotificationWrapper
import com.example.contextualtriggers.context.ContextHolder
import java.lang.Thread.sleep

class TriggerManager(context:Context, private val contextHolder: ContextHolder) {


    private val notificationWrapper = NotificationWrapper(context)

    private val trigger:MutableList<Trigger> = mutableListOf()


    init{
          val batteryTrigger= BatteryTrigger(contextHolder)
          //trigger.add(batteryTrigger)
          val activityRecoginitionTrigger=ActivityRecoginitionTrigger(contextHolder)
          //trigger.add(activityRecoginitionTrigger)

          val calendarTrigger= CalendarTrigger(contextHolder)
         // trigger.add(calendarTrigger)
          val stepCounterTrigger=StepCounterTrigger(contextHolder)
         // trigger.add(stepCounter)
          val weatherTrigger= WeatherTrigger(contextHolder)
         // trigger.add(weatherTrigger)

          val temperatureTrigger= TemperatureTrigger(contextHolder)
        //  trigger.add(temperatureTrigger)
          val timeTrigger=TimeTrigger(contextHolder)
                //trigger.add(timeTrigger)

            //Complex Triggers
        val weatherTemperatureTriggerList = listOf(weatherTrigger, temperatureTrigger,batteryTrigger)
            val weatherTemperatureTrigger = WeatherTemperatureTrigger(weatherTemperatureTriggerList)
             trigger.add(weatherTemperatureTrigger)
          val stepsTemperatureTriggerList = listOf(stepCounterTrigger, temperatureTrigger, batteryTrigger)
          val stepsTemperatureTrigger = StepsTemperatureTrigger(stepsTemperatureTriggerList)
            trigger.add(stepsTemperatureTrigger)

          val timeWeatherTriggerList = listOf(timeTrigger, weatherTrigger, batteryTrigger)
          val timeWeatherTrigger = TimeWeatherTrigger(timeWeatherTriggerList)
            trigger.add(timeWeatherTrigger)

        val activityCalendarTriggerList = listOf(activityRecoginitionTrigger, calendarTrigger, batteryTrigger)
        val activityCalendarTrigger = ActivityCalendarTrigger(activityCalendarTriggerList)
            trigger.add(activityCalendarTrigger)
      Log.d("stepCounterTrigger","$trigger")





  }
    fun handleNotification() {
        var highestPriority = Int.MIN_VALUE
        var highestPriorityTrigger: Trigger? = null

        for( t in trigger) {
            val isTriggered=t.isTriggered()
            if (isTriggered) {
                val priority = t.getPriority()
                if (priority > highestPriority) {
                    highestPriority = priority
                    highestPriorityTrigger = t
                }
            }
        }
        if (highestPriorityTrigger != null) {
            val notificationTitle = highestPriorityTrigger.getNotificationTitle()
            val notificationMessage = highestPriorityTrigger.getNotificationMessage()
            notificationWrapper.showNotification(notificationTitle, notificationMessage)
            sleep(1000)
        }
    }






}