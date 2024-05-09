package com.example.vaccinationapp.VaccineControl

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

/**
 * Activity for displaying a pop-up window to confirm adding a vaccine.
 */
class PopUpWindow : AppCompatActivity() {

    // TextView for displaying vaccine name
    lateinit var getName: TextView
    // TextView for displaying vaccine date
    lateinit var getDate: TextView
    // TextView for displaying vaccine time
    lateinit var getHour: TextView
    // Button adding vaccine to database
    lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popupwindow)

        // Initialize views
        getDate = findViewById(R.id.getVaccineDate)
        getHour = findViewById(R.id.getVaccineHour)
        getName = findViewById(R.id.getVaccineName)
        confirmButton = findViewById(R.id.confirm_button)

        // Retrieve data from intent
        val vaccineNameSpinner = intent.getStringExtra("VACCINENAME")
        val editTextDate = intent.getSerializableExtra("DATE") as Date
        val editTextHour = intent.getStringExtra("HOUR")

        // Set retrieved data to corresponding views
        getName.text = vaccineNameSpinner
        getDate.text = editTextDate.toString()
        getHour.text = editTextHour

        // Set up click listener for close button
        val close = findViewById<ImageView>(R.id.closePopUpWindow)
        close.setOnClickListener {
            finish()
        }

        // Set up click listener for confirm button
        confirmButton.setOnClickListener {
            addVaccineToDatabase(editTextDate)
        }
    }

    /**
     * Adds vaccine to Database (AddVaccineDataClass) object
     * @param vaccineDate - date of the vaccine
     */
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

    /**
     * Retrieves the ID of the selected vaccine from the database
     *
     * @param vaccineName the name of the selected vaccine
     * @return the Id of the selected vaccine
     */

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
    /**
     * Generates a unique ID for the vaccine record
     *
     * @return  unique Id for the vaccine record
     */
    private fun generateId(): String {
        return UUID.randomUUID().toString()
    }

}