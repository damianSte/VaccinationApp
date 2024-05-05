package com.example.vaccinationapp.VaccineControl.AddingVaccines

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccinationapp.R
import com.example.vaccinationapp.phpAdmin.DataClasses.AddVaccineDataClass
import com.example.vaccinationapp.phpAdmin.DBConnection
import com.example.vaccinationapp.phpAdmin.DBQueries
import com.example.vaccinationapp.phpAdmin.UserData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Date

import java.util.UUID

class PopUpWindow : AppCompatActivity() {

    lateinit var getName: TextView
    lateinit var getDate: TextView
    lateinit var getHour: TextView
    lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popupwindow)

        getDate = findViewById(R.id.getVaccineDate)
        getHour = findViewById(R.id.getVaccineHour)
        getName = findViewById(R.id.getVaccineName)
        confirmButton = findViewById(R.id.confirm_button)

        val vaccineNameSpinner = intent.getStringExtra("VACCINENAME")
        val editTextDate = intent.getSerializableExtra("DATE") as Date
        val editTextHour = intent.getStringExtra("HOUR")

        val close = findViewById<ImageView>(R.id.closePopUpWindow)

        getName.text = vaccineNameSpinner
        getDate.text = editTextDate.toString()
        getHour.text = editTextHour

        close.setOnClickListener {
            finish()
        }
        confirmButton.setOnClickListener {
            addVaccineToDatabase(editTextDate)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun addVaccineToDatabase(vaccineDate: Date) {
        val vaccineName = getName.text.toString()
        val vaccineTime = getHour.text.toString()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val vaccineId = addVaccineId(vaccineName)
                val vaccineRecordId = generateId()
                val userId = UserData.getUserId()

                if (vaccineId != null) {
                    val connection = DBConnection.getConnection()
                    val dbQueries = DBQueries(connection)

                    val newVaccine = AddVaccineDataClass(vaccineId, vaccineRecordId, userId, vaccineDate,vaccineTime )
                    dbQueries.insertVaccine(newVaccine)

                    connection.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        finish()
    }

    private fun addVaccineId(vaccineName: String): String? {
        return try {
            val connection = DBConnection.getConnection()
            val dbQueries = DBQueries(connection)

            val vaccineId = dbQueries.getVaccineId(vaccineName)

            connection.close()

            vaccineId
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun generateId(): String {
        return UUID.randomUUID().toString()
    }

}