package com.example.vaccinationapp.phpAdmin

interface DbUser {

    fun getPassword(password: String): LogInDataClass?
    fun getEmail(email: String) : LogInDataClass?
    fun getUserId(id: String): LogInDataClass?
    fun insertUser(user: LogInDataClass): Boolean
}