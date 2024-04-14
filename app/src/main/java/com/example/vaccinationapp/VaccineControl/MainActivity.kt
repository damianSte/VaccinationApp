package com.example.vaccinationapp.VaccineControl

import android.os.Bundle
import com.example.vaccinationapp.Functional.BarHandler
import com.example.vaccinationapp.R

class MainActivity : BarHandler() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openActivity(R.id.bottom_home)

    }
}