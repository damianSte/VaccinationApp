package com.example.vaccinationapp.phpAdmin

interface DbUser {

    fun getPassword(password: String): SignUpDataClass?
   // fun getUser(email: String, password: String): LogInDataClass?
    fun insertUser(user: SignUpDataClass): Boolean
    fun userExists(email: String, password: String): Boolean




}