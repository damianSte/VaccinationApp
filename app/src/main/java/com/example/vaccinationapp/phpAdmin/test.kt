package com.example.vaccinationapp.phpAdmin

import java.security.MessageDigest

fun main() {

        try {
            val connection = DBConnection.getConnection()
            val dbQueries = DBQueries(connection)

            val email = hashData("damian@damian.pl")
            val password = hashData("Damian")
            val userExists = dbQueries.userExists(email, password)
            println("User exists: $userExists")

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