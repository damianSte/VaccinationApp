package com.example.vaccinationapp.phpAdmin

import java.security.MessageDigest
import java.util.UUID

fun main() {


    val recordId = "052076e6-b23a-4a69-bfd0-d07afc940465"

    try {
        val connection = DBConnection.getConnection()

        val dbQueries = DBQueries(connection)


        dbQueries.deleteVaccineRecord(recordId)
        connection.close()


    } catch (e: Exception) {
        e.printStackTrace()
    }

}

private fun hashData(data: String): String {
    val bytes = data.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.toHexString()
}

fun ByteArray.toHexString(): String {
    return joinToString("") { "%02x".format(it) }
}

private fun generateUserId(): String {
    return UUID.randomUUID().toString()
}


private fun getUserProfileValues() {
    val userId = "00403d88-a5eb-46bb-bdba-eaa9d30c1972"


    try {
        val connection = DBConnection.getConnection()

        val dbQueries = DBQueries(connection)

        if (userId != null) {
            dbQueries.getUserProfile(userId)
            connection.close()

        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

