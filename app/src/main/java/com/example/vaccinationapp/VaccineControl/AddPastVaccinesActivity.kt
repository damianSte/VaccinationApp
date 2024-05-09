package com.example.vaccinationapp.VaccineControl

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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

class AddPastVaccinesActivity : BarHandler() {

    private lateinit var vaccineName: Spinner
    private lateinit var date: EditText
    private lateinit var updateButton: Button
    private var selectedDateCalendar: Calendar = Calendar.getInstance() // Initialize Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_past_vaccines)

        openActivity(R.id.bottom_base)

        // Initialize the Spinner
        vaccineName = findViewById(R.id.vaccine_choose)
        // Initialize EditTexts Data, Hour
        date = findViewById(R.id.date_choose)
        updateButton = findViewById(R.id.addVaccine)

        // Parse CSV and extract vaccine names with IDs
        val vaccineOptions = parseCSVAndExtractVaccineNames()

        // Extract vaccine names for display in the Spinner
        val vaccineNames = vaccineOptions.map { it.second }

        // Extract vaccine IDs for database storage
        val vaccineIDs = vaccineOptions.map { it.first }

        // Populate the Spinner with vaccine options
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vaccineNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vaccineName.adapter = adapter

        date.setOnClickListener {
            showDatePicker()
        }

        updateButton.setOnClickListener {
            val selectedSqlDate = Date(selectedDateCalendar.timeInMillis)
            addVaccineToDatabase(selectedSqlDate)
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
                // Do something with the selected vaccine name and ID
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        })
    }

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

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // Adjust month +1 as months are zero-based in Calendar
                selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay)
                val formattedDate =
                    String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                date.setText(formattedDate)
            }, year, month, dayOfMonth
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

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
