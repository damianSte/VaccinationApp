package com.example.vaccinationapp.VaccineControl

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.vaccinationapp.Functional.BarHandler
import com.example.vaccinationapp.R

class MainActivity : BarHandler() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openActivity(R.id.bottom_home)

        var check = findViewById<TextView>(R.id.vaccine_name_edit)

        val checkin = intent.getStringExtra("ID").toString()

        check.text = checkin

    }
}