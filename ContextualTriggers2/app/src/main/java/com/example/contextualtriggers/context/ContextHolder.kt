package com.example.contextualtriggers.context

import java.util.concurrent.CompletableFuture

interface ContextHolder {

    fun getBatteryLevel(): Int
    fun getTemperature(): Double
    fun getCurrentTime(): Int
    fun sendSteps(): CompletableFuture<Int>
    fun getDetectedActivity():CompletableFuture<String>
    fun getWeather(): String
    fun getIsCalendarEmpty(): String


}
