package com.example.vaccinationapp.VaccineControl

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.example.vaccinationapp.R
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.vaccinationapp.Functional.BarHandler

class AddVaccineActivity : BarHandler() {

    lateinit var chooseVaccine: Spinner
    lateinit var chooseDate: EditText
    lateinit var chooseHour: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vaccine)


        openActivity(R.id.bottom_add)

        // Initialize the Spinner
        chooseVaccine = findViewById(R.id.vaccine_choose)

        // Initialize EditTexts Data, Hour
        chooseDate = findViewById(R.id.date_choose)
        chooseHour = findViewById(R.id.hour_choose)

        val selectDateButton = findViewById<Button>(R.id.selectDate)

        // Parse CSV and extract vaccine names with IDs
        val vaccineOptions = parseCSVAndExtractVaccineNames()

        // Extract vaccine names for display in the Spinner
        val vaccineNames = vaccineOptions.map { it.second }

        // Extract vaccine IDs for database storage
        val vaccineIDs = vaccineOptions.map { it.first }

        // Populate the Spinner with vaccine options
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vaccineNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        chooseVaccine.adapter = adapter

        selectDateButton.setOnClickListener {
            openPopupActivity()
        }

        // Set OnClickListener to show date picker dialog
        chooseDate.setOnClickListener {
            showDatePicker()
        }

        chooseHour.setOnClickListener {
            showTimePicker()
        }

        // Optionally handle selection events
        chooseVaccine.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
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
                // Adjust month +1 as months are zero-based in Calendar
                val formattedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                chooseDate.setText(formattedDate)
            }, year, month, dayOfMonth)

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    /**
     * Displays a TimePickerDialog  for selecting time
     */
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, selectedHourOfDay, selectedMinute ->
                if (isValidTime(selectedHourOfDay)) {
                    val formattedTime = String.format("%02d:%02d", selectedHourOfDay, selectedMinute)
                    chooseHour.setText(formattedTime)
                } else {
                    Toast.makeText(this, "Please select a time between 8 AM and 5 PM", Toast.LENGTH_SHORT).show()
                    showTimePicker()
                }
            }, hourOfDay, minute, true)

        timePickerDialog.show()
    }

    /**
     * isValidTime checking if time is valid
     */

    private fun isValidTime(hourOfDay: Int): Boolean {
        return hourOfDay in 8..18
    }

    /**
     * Opening PopUpWindow Activity sending vlaues
     */
    private fun openPopupActivity() {
        val selectedDate = chooseDate.text.toString()
        val dateParts = selectedDate.split("-")
        val year = dateParts[0].toInt()
        val month = dateParts[1].toInt() - 1 // Adjust month to zero-based
        val day = dateParts[2].toInt()

        val selectedDateCalendar = Calendar.getInstance()
        selectedDateCalendar.set(year, month, day)

        val selectedSqlDate = java.sql.Date(selectedDateCalendar.timeInMillis)

        val intent = Intent(this, PopUpWindow::class.java).apply {
            putExtra("VACCINENAME", chooseVaccine.selectedItem.toString())
            putExtra("DATE", selectedSqlDate)
            putExtra("HOUR", chooseHour.text.toString())
        }
        startActivity(intent)

        chooseVaccine.setSelection(0)
        chooseDate.text.clear()
        chooseHour.text.clear()

    }


}

