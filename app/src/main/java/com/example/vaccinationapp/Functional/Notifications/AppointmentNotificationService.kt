package com.example.vaccinationapp.Functional.Notifications

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.vaccinationapp.phpAdmin.UserData
import java.util.*

class AppointmentNotificationService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        // Get user ID
        val userId = UserData.getUserId()

        // Get appointment details
        val db = Notification(context)
        val appointmentDetails = userId?.let { db.getAppointmentTime(it) }

        // Schedule notification 24 hours before the appointment
        appointmentDetails?.let { (appointmentDate, appointmentTime) ->
            scheduleNotification(context, appointmentDate, appointmentTime)
        }
    }
    fun scheduleNotification(context: Context, appointmentDate: String, appointmentTime: String) {
        val notificationManager = NotificationManagerCompat.from(context)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.DATE, -1)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

}
