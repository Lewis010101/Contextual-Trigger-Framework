package com.example.contextualtriggers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class CTBroadCastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent)
    {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, MainActivity::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }
}