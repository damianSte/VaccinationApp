package com.example.vaccinationapp.Functional

import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

open class HashClass : AppCompatActivity() {

    fun hashData(data: String): String {
        val bytes = data.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.toHexString()
    }

    fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

}