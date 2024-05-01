package com.example.vaccinationapp.VaccineControl


import android.os.Bundle
import com.example.vaccinationapp.Functional.BarHandler
import com.example.vaccinationapp.R


class VaccineHistoryActivity : BarHandler() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine_history)

        openActivity(R.id.bottom_history)
    }
}