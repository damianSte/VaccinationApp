package com.example.vaccinationapp.phpAdmin

import com.example.vaccinationapp.VaccineControl.AddVaccineActivity

interface DbUser {

    fun insertUser(user: SignUpDataClass): Boolean
    fun userExists(email: String, password: String): Boolean

    fun insertVaccine( vaccine: AddVaccineDataClass):Boolean
}