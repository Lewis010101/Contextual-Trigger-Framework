package com.example.contextualtriggers.context.contextData

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import java.util.*

class SleepTracker(private val context: Context): SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val ambientLightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    private val gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    private val magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private var lastAccelerometerReading = FloatArray(3)
    private var lastAmbientLightReading: Int = 0
    private var lastGyroscopeReading = FloatArray(3)
    private var lastMagnetometerReading = FloatArray(3)

    private var sleepStartTime: Long = 0
    private var sleepEndTime: Long = 0

    private var isSleeping = false

    init {
        start()
    }

    fun start() {
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, ambientLightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stop() {
        sensorManager.unregisterListener(this, accelerometerSensor)
        sensorManager.unregisterListener(this, ambientLightSensor)
        sensorManager.unregisterListener(this, gyroscopeSensor)
        sensorManager.unregisterListener(this, magnetometerSensor)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                lastAccelerometerReading = event.values
            }
            Sensor.TYPE_LIGHT -> {
                lastAmbientLightReading = event.values[0].toInt()
            }
            Sensor.TYPE_GYROSCOPE -> {
                lastGyroscopeReading = event.values
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                lastMagnetometerReading = event.values
            }


        }


        // Determine if the user is sleeping based on the sensor readings
        if (lastAccelerometerReading[2] < 8 && lastAmbientLightReading < 10) {
            if (!isSleeping) {
                // User has started sleeping
                isSleeping = true
                sleepStartTime = Calendar.getInstance().timeInMillis
            }
        } else {
            if (isSleeping) {
                // User has stopped sleeping
                isSleeping = false
                sleepEndTime = Calendar.getInstance().timeInMillis
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do nothing
    }

    fun getSleepTime(): Long {
        Log.d("SleepTime", "getSleepTime called")
        Log.d("mLastAccelerometerReading Detected","$lastAccelerometerReading")

        Log.d("lastAmbientLightReading","$lastAmbientLightReading")
        Log.d("lastGyroscopeReading","$lastGyroscopeReading")
        Log.d("mLastMagnetometerReading","$lastMagnetometerReading")
        return sleepEndTime - sleepStartTime
    }

    fun getAccelerometerReading(): FloatArray {
        return lastAccelerometerReading
    }

    fun getAmbientLightReading(): Int {
        return lastAmbientLightReading
    }

    fun getGyroscopeReading(): FloatArray {
        return lastGyroscopeReading
    }

    fun getMagnetometerReading(): FloatArray {
        return lastMagnetometerReading
    }
}
