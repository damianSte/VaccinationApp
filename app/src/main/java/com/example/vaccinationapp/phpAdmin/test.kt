package com.example.vaccinationapp.phpAdmin

import java.security.MessageDigest
import java.sql.SQLException
import java.util.UUID

fun main() {
    try {
        val email = "dupa1@up.pl"
        val password = "12345678"

        val emailHashed = hashData(email)
        val passHashed = hashData(password)

        val connection = DBConnection.getConnection()
        val dbQueries = DBQueries(connection)

        val userExists = dbQueries.userExists(emailHashed, passHashed)

        connection.close()

        if (userExists) {
            println("User found")
        } else {
            println("User not found")
        }
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
