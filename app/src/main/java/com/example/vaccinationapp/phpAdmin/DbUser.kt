package com.example.vaccinationapp.phpAdmin

import com.example.vaccinationapp.phpAdmin.DataClasses.AddVaccineDataClass
import com.example.vaccinationapp.phpAdmin.DataClasses.SignUpDataClass

interface DbUser {

    fun insertUser(user: SignUpDataClass): Boolean
    fun userExists(email: String, password: String): Boolean
    fun insertVaccine( vaccine: AddVaccineDataClass):Boolean
}