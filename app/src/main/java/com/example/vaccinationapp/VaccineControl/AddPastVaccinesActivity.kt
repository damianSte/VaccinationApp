package com.example.vaccinationapp.VaccineControl

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.vaccinationapp.Functional.BarHandler
import com.example.vaccinationapp.R
import com.example.vaccinationapp.phpAdmin.DBConnection
import com.example.vaccinationapp.phpAdmin.DBQueries
import com.example.vaccinationapp.phpAdmin.DataClasses.AddVaccineDataClass
import com.example.vaccinationapp.phpAdmin.UserData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.Calendar
import java.util.UUID

/**
 * Activity class responsible for adding past vaccines to the database.
 *
 * This class facilitates the addition of past vaccines by allowing users to select a vaccine from a Spinner,
 * choose a date, and submit the information to the database. The selected vaccine's ID, along with other
 * relevant data, is stored in the database.
 */
class AddPastVaccinesActivity : BarHandler() {

    // Views
    // Spinner for selecting vaccine (name)
    private lateinit var vaccineName: Spinner
    // EditText for selecting date
    private lateinit var date: EditText
    // Button for adding to database
    private lateinit var updateButton: Button

    // Calendar for storing selected date
    private var selectedDateCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_past_vaccines)

        openActivity(R.id.bottom_base)

        // Initialize Views
        vaccineName = findViewById(R.id.vaccine_choose)
        date = findViewById(R.id.date_choose)
        updateButton = findViewById(R.id.addVaccine)

        // Parse CSV and extract vaccine names
        val vaccineOptions = parseCSVAndExtractVaccineNames()

        // Extract vaccine names for display in the Spinner
        val vaccineNames = vaccineOptions.map { it.second }

        // Extract vaccine IDs for database storage
        val vaccineIDs = vaccineOptions.map { it.first }

        // Populate the Spinner with vaccine options
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vaccineNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vaccineName.adapter = adapter

        // Set click listener for date EditText
        date.setOnClickListener {
            showDatePicker()
        }

        // Set click listener for update button
        updateButton.setOnClickListener {
            val selectedSqlDate = Date(selectedDateCalendar.timeInMillis)
            addVaccineToDatabase(selectedSqlDate)
            vaccineName.setSelection(0)
            date.text.clear()
            makeToast("Vaccine has been added", false)
        }

        // Optionally handle selection events
        vaccineName.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Perform action based on selection
                val selectedVaccineName = vaccineNames[position]
                val selectedVaccineID = vaccineIDs[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })
    }

    /**
     * parseCSVAndExtractVaccineNames parses CSV file containning vaccine information and extracts vaccine names
     * @return A list of pairs containing vaccines IDs and names
     */

    private fun parseCSVAndExtractVaccineNames(): List<Pair<String, String>> {
        val vaccineNames = mutableListOf<Pair<String, String>>()
        val csvData = """
               ,    ,    ,   
           001,Adenovirus,Barr Labs Inc., 1
002,Anthrax,Emergent BioSolutions,3
003,Cholera,PaxVax, 1
004,COVID-19,Pfizer, 2
005,Dengue,ModernaTx, 3
006,DTaP,Sanofi, 5
007,DT,Sanofi, 5
008,Haemophilus influenzae type b (Hib),GlaxoSmithKline, 4
009,Hepatitis A,Merck, 2
010,Hepatitis B,Merck, 3
011,Herpes Zoster (Shingles),GlaxoSmithKline,2
012,Human Papillomavirus (HPV),Merck, 2 or 3
013,Influenza,Seqirus, 1 or 2
014,Japanese encephalitis,Valneva, 2
015,Measles; Mumps; Rubella,Merck, 2
016,Meningococcal,Sanofi, 2
017,Pneumococcal,Pfizer, 4
018,Polio,Sanofi, 4
019,Rabies,GlaxoSmithKline, 3
020,Rotavirus,Merck, 3
021,Tetanus;(reduced) Diphtheria,Massachusetts Biological Labs, 1
022,Typhoid,PaxVax, 4
023,Varicella,Merck, 2
024,Vaccinia (Smallpox),Sanofi, 1
025,Yellow Fever,Sanofi, 1
        """.trimIndent()
        val lines = csvData.split("\n")
        for (line in lines) {
            val columns = line.split(",")
            if (columns.size >= 3) {
                val vaccineID = columns[0].trim()
                val vaccineName = columns[1].trim()
                vaccineNames.add(Pair(vaccineID, vaccineName))
            }
        }
        return vaccineNames
    }

    /**
     * Displays a DatePickerDialog  for selecting date
     */

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // Adjust month +1 as months are zero in Calendar
                selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay)
                val formattedDate =
                    String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                date.setText(formattedDate)
            }, year, month, dayOfMonth
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    /**
     * addVaccineToDatabase adds selected vaccine, data, and hour to database
     * @param vaccineDate The date of the vaccine.
     */

    @OptIn(DelicateCoroutinesApi::class)
    private fun addVaccineToDatabase(vaccineDate: Date) {
        val name = vaccineName.selectedItem.toString()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val vaccineId = addVaccineId(name)
                val vaccineRecordId = generateId()
                val userId = UserData.getUserId()

                if (vaccineId != null) {
                    val connection = DBConnection.getConnection()
                    val dbQueries = DBQueries(connection)

                    val newVaccine =
                        AddVaccineDataClass(vaccineId, vaccineRecordId, userId, vaccineDate, null)
                    dbQueries.insertVaccine(newVaccine)

                    connection.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Retrieves the ID of the selected vaccine from the database.
     *
     * @param vaccineName The name of the selected vaccine.
     * @return The ID of the selected vaccine.
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
     * Generates a unique ID for the vaccine record.
     *
     * @return A unique ID for the vaccine record.
     */
    private fun generateId(): String {
        return UUID.randomUUID().toString()
    }

    private fun makeToast(toast: String, errorMessage: Boolean) {
        Toast.makeText(this@AddPastVaccinesActivity, toast, Toast.LENGTH_LONG).show()
    }
}
