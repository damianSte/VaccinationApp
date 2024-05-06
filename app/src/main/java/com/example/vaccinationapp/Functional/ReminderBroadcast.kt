package com.example.vaccinationapp.Functional

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.vaccinationapp.R

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationBuilder = NotificationCompat.Builder(context, "ChannelId")
            .setSmallIcon(R.drawable.vaccine_12111387)
            .setContentTitle("Vaccination App")
            .setContentText("Vaccine Reminder")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val manager = NotificationManagerCompat.from(context)
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        manager.notify(200, notificationBuilder.build())
    }
}

