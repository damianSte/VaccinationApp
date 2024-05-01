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


        // Parse CSV and extract vaccine names
        val vaccineOptions = parseCSVAndExtractVaccineNames()
        // Initialize button for appointment selection
        val selectDatebutton = findViewById<Button>(R.id.selectDate)
        // Populate the Spinner with vaccine options
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vaccineOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        chooseVaccine.adapter = adapter

        selectDatebutton.setOnClickListener {
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
                val selectedVaccine = vaccineOptions[position]
                // Do something with the selected vaccine
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        })
    }

    private fun parseCSVAndExtractVaccineNames(): List<String> {
        val vaccineNames = mutableListOf<String>()
        val csvData = """
            Adenovirus,Barr Labs Inc.
            Anthrax,Emergent BioSolutions
            Cholera,PaxVax
            COVID-19,Pfizer
            Dengue,ModernaTx
            DTaP,Sanofi
            DT,Sanofi
            Haemophilus influenzae type b (Hib),GlaxoSmithKline
            Hepatitis A,Merck
            Hepatitis B,Merck
            Herpes Zoster (Shingles),GlaxoSmithKline
            Human Papillomavirus (HPV),Merck
            Influenza,Seqirus
            Japanese encephalitis,Valneva
            Measles; Mumps; Rubella,Merck
            Meningococcal,Sanofi
            Pneumococcal,Pfizer
            Polio,Sanofi
            Rabies,GlaxoSmithKline
            Rotavirus,Merck
            Tetanus; (reduced) Diphtheria,Massachusetts Biological Labs
            Typhoid,PaxVax
            Varicella,Merck
            Vaccinia (Smallpox),Sanofi
            Yellow Fever,Sanofi
        """.trimIndent()
        val lines = csvData.split("\n")
        for (line in lines) {
            val columns = line.split(",")
            if (columns.size >= 2) {
                val vaccineName = columns[0].trim()
                vaccineNames.add(vaccineName)
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
                val formattedDate = "$selectedDay.${selectedMonth + 1}.$selectedYear"
                chooseDate.setText(formattedDate)
            }, year, month, dayOfMonth)

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

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

    private fun isValidTime(hourOfDay: Int): Boolean {
        return hourOfDay in 8..17
    }

    private fun openPopupActivity() {
        val intent = Intent(this, PopUpWindow::class.java).apply {
            putExtra("VACCINENAME", chooseVaccine.selectedItem.toString())
            putExtra("DATE", chooseDate.text.toString())
            putExtra("HOUR", chooseHour.text.toString())
        }
        startActivity(intent)
    }



}
