package com.example.vaccinationapp.phpAdmin

import com.example.vaccinationapp.phpAdmin.DataClasses.AddVaccineDataClass
import com.example.vaccinationapp.phpAdmin.DataClasses.UserProfileDataClass
import java.security.MessageDigest
import java.sql.SQLException
import java.util.UUID

fun main() {

    val id = "00403d88-a5eb-46bb-bdba-eaa9d30c1972"

    val connection = DBConnection.getConnection()
    val dbQueries = DBQueries(connection)

    dbQueries.getVaccineHistory(id)


    connection.close()

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
