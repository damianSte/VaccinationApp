package com.example.vaccinationapp.VaccineControl

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccinationapp.R
import com.example.vaccinationapp.phpAdmin.AddVaccineDataClass
import com.example.vaccinationapp.phpAdmin.DBConnection
import com.example.vaccinationapp.phpAdmin.DBQueries
import com.example.vaccinationapp.phpAdmin.UserData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

        val spinnerValue = intent.getStringExtra("VACCINENAME")
        val editTextDate = intent.getStringExtra("DATE")
        val editTextHour = intent.getStringExtra("HOUR")


        val close = findViewById<ImageView>(R.id.closePopUpWindow)

        getName.text = spinnerValue
        getDate.text = editTextDate
        getHour.text = editTextHour


        close.setOnClickListener {
            finish()
        }
        confirmButton.setOnClickListener{
            addVaccineToDatabase()
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun addVaccineToDatabase() {

        val vaccineId = generateId()
        val vaccineRecordId = generateId()
        val userId = UserData.getUserId()
        val vaccineDate = intent.getStringExtra("DATE").toString()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = DBConnection.getConnection()
                val dbQueries = DBQueries(connection)

                val newVaccine = AddVaccineDataClass(vaccineId, vaccineRecordId, userId, vaccineDate)
                dbQueries.insertVaccine(newVaccine)

                connection.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun generateId(): String {
        return UUID.randomUUID().toString()
    }

}