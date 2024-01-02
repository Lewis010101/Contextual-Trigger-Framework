package com.example.contextualtriggers


import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager

import java.util.concurrent.TimeUnit

class MainActivity: AppCompatActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private var isActivityRecognitionPermissionGranted =false
    private var isCalendarReadPermissionGranted =false
    private var isCalendarWritePermissionGranted =false
    private var isLocationPermissionFineGranted= false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        {
                permissions->
            isActivityRecognitionPermissionGranted=permissions[  android.Manifest.permission.ACTIVITY_RECOGNITION]?:
                    isActivityRecognitionPermissionGranted

            isCalendarReadPermissionGranted=permissions[  android.Manifest.permission.READ_CALENDAR]?:
                    isCalendarReadPermissionGranted

            isCalendarWritePermissionGranted=permissions[  android.Manifest.permission.WRITE_CALENDAR]?:
                    isCalendarWritePermissionGranted



        }
        requestPermission()

        Log.d("ApplicationDBInitializer", "Database initialization")





        val workRequest = PeriodicWorkRequestBuilder<CTWorker>(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "CTWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )


        finish()
    }

    private fun requestPermission() {
        isActivityRecognitionPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED

        isCalendarReadPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED

        isCalendarWritePermissionGranted = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED


        val permissionRequestList: MutableList<String> = ArrayList()

        if (!isActivityRecognitionPermissionGranted && !isCalendarReadPermissionGranted && !isCalendarWritePermissionGranted
            &&!isLocationPermissionFineGranted) {
            permissionRequestList.add(  android.Manifest.permission.ACTIVITY_RECOGNITION)
            permissionRequestList.add(  android.Manifest.permission.READ_CALENDAR)
            permissionRequestList.add(  android.Manifest.permission.WRITE_CALENDAR)

        }

        if(permissionRequestList.isNotEmpty())
        {
            permissionLauncher.launch(permissionRequestList.toTypedArray())
        }
    }
}