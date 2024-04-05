package com.example.vaccinationapp.phpAdmin

interface DbUser {

    fun insertUser(user: SignUpDataClass): Boolean
    fun userExists(email: String, password: String): Boolean




}