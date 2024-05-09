package com.example.vaccinationapp.Functional.Notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.vaccinationapp.phpAdmin.DBConnection
import java.util.*

class Notification(private val context: Context) {

    fun getAppointmentTime(userId: String): Pair<String, String>? {
        val connection = DBConnection.getConnection()

        // Perform the database query
        val call = "{CALL getAppointmentTime(?)}"
        val statement = connection.prepareCall(call)
        statement.setString(1, userId)

        val resultSet = statement.executeQuery()

        var appointmentDate: String? = null
        var appointmentTime: String? = null

        if (resultSet.next()) {
            appointmentDate = resultSet.getString("date_of_vaccine")
            appointmentTime = resultSet.getString("hour")
        }

        resultSet.close()
        statement.close()

        // Check if appointment details were retrieved
        if (appointmentDate != null && appointmentTime != null) {
            return Pair(appointmentDate, appointmentTime)
        } else {
            return null
        }
    }
}

