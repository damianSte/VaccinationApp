package com.example.vaccinationapp.phpAdmin

import java.security.MessageDigest
import java.sql.SQLException
import java.util.UUID

fun main() {
    try {
        val vaccineId = generateUserId()
        val vaccineRecordId = generateUserId()
        val userId = "00403d88-a5eb-46bb-bdba-eaa9d30c1972"
        val vaccineDate = "1.01.2023"

        val connection = DBConnection.getConnection()
        val dbQueries = DBQueries(connection)

        val newVaccine = AddVaccineDataClass(vaccineId, vaccineRecordId, userId, vaccineDate)
        dbQueries.insertVaccine(newVaccine)

        connection.close()

    } catch (e: SQLException) {
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
