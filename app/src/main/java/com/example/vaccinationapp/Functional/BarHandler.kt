package com.example.vaccinationapp.Functional

import android.content.Intent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccinationapp.R
import com.example.vaccinationapp.VaccineControl.AddVaccineActivity
import com.example.vaccinationapp.VaccineControl.MainActivity
import com.example.vaccinationapp.VaccineControl.VaccineHistoryActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BarHandler : AppCompatActivity() {

    fun openActivity(buttonId: Int) {

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationBar)
        bottomNavigationView.selectedItemId = buttonId

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true // Return true to indicate that the item click is handled
                }

                R.id.bottom_add -> {
                    val intent = Intent(this, AddVaccineActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.bottom_history -> {
                    val intent = Intent(this, VaccineHistoryActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false // Return false if the item click is not handled
            }

        }
    }
}