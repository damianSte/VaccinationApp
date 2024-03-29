package com.example.vaccinationapp.phpAdmin

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DBConnection {

        // Database connection details
        private const val URL =
            "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11692905?useUnicode=true&characterEncoding=utf-8&serverTimezone=CET" //
        private const val USER = "sql11692905"
        private const val PASS = "Yh31wh8iun"
    init {
        Class.forName("com.mysql.jdbc.Driver")
    }
    fun getConnection(): Connection {
        try {
            return DriverManager.getConnection(URL, USER, PASS)
        } catch (ex: SQLException) {
            throw RuntimeException("Error connecting to the database", ex)
        }
    }
    // Main function to test the database connection
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            // Getting a connection
            val conn = getConnection()
            // Closing the connection
            conn.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}

