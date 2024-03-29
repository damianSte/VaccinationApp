package com.example.vaccinationapp.phpAdmin

import java.util.UUID

fun main() {
    try {
        // Getting connection using DBConnection class
        val connection = DBConnection.getConnection() // Getting

        val dbQueries = DBQueries(connection) // Creating a

        val newUser = LogInDataClass(generateUserId(), "test@emial.com", "password")
        println("Insertion successful:${dbQueries.insertUser(newUser)}")
        connection.close()

    } catch (e: Exception) {
        e.printStackTrace()
    }

}


private fun generateUserId(): String {
    return UUID.randomUUID().toString()
}
