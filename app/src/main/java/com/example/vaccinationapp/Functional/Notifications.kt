package com.example.vaccinationapp.Functional

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccinationapp.R
import com.example.vaccinationapp.phpAdmin.DBConnection
import com.example.vaccinationapp.phpAdmin.DBQueries
import com.example.vaccinationapp.phpAdmin.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class Notifications: AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            getNotificationTime()

        }

    private fun scheduleNotification(selectedTime: String) {
        val intentNot = Intent(this, ReminderBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intentNot,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()

        // Parse selected time to set the notification delay
        val selectedTimeParts = selectedTime.split(":")
        calendar.set(Calendar.HOUR_OF_DAY, selectedTimeParts[0].toInt())
        calendar.set(Calendar.MINUTE, selectedTimeParts[1].toInt())

        val selectedTimeMillis = calendar.timeInMillis

        // Calculate the delay based on the selected time
        val delayMillis = selectedTimeMillis - System.currentTimeMillis()

        // Set the alarm with the calculated delay
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + delayMillis,
            pendingIntent
        )
    }

    private fun getNotificationTime() {
        val userId = UserData.getUserId()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = DBConnection.getConnection()
                val dbQueries = DBQueries(connection)

                if (userId != null) {
                    val appointmentTime = dbQueries.getAppointmentTime(userId)
                    scheduleNotification(appointmentTime)
                }
                connection.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}