package com.example.vaccinationapp.phpAdmin.DataClasses

import java.sql.Date

data class AddVaccineDataClass(

    val vaccineId: String? = null,
    val recordId: String? = null,
    val userId: String? = null,
    val dateOfVaccine: Date? = null,
    val timeOfVaccine: String? = null


)

