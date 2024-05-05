package com.example.vaccinationapp.phpAdmin.DataClasses

import java.sql.Date

data class VaccineDataClass(
    val name: String,
    val manufacturer: String,
    val lastDose: Date,
    val time: String? = null
)
