package com.example.vaccinationapp.VaccineControl

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Spinner
import com.example.vaccinationapp.R
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccinationapp.Functional.BarHandler

class AddVaccineActivity : BarHandler() {

    lateinit var chooseVaccine: Spinner
    lateinit var chooseDate: EditText
    lateinit var hourChoose: EditText
    lateinit var getName: EditText
    lateinit var getDate: EditText
    lateinit var getHour: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vaccine)

        openActivity(R.id.bottom_add)
        // Initialize the Spinner
        chooseVaccine = findViewById(R.id.vaccine_choose)

        // Parse CSV and extract vaccine names
        val vaccineOptions = parseCSVAndExtractVaccineNames()

        // Populate the Spinner with vaccine options
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vaccineOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        chooseVaccine.adapter = adapter

        // Initialize the EditText for selecting date
        chooseDate = findViewById(R.id.date_choose)
        hourChoose = findViewById(R.id.hour_choose)

//        getDate = findViewById(R.id.getVaccineDate)
//        getHour = findViewById(R.id.getVaccineHour)
//        getName = findViewById(R.id.getVaccineName)

        // Set OnClickListener to show date picker dialog
        chooseDate.setOnClickListener {
            showDatePicker()
        }

        hourChoose.setOnClickListener { showTimePicker() }

        val button = findViewById<Button>(R.id.selectDate)

        button.setOnClickListener {
            showPopupWindow()
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
                    hourChoose.setText(formattedTime)
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

    private fun showPopupWindow() {
        // Initialize a new instance of PopupWindow
        val popupWindow = PopupWindow(this)

        // Inflate the layout for the popup window
        val view = layoutInflater.inflate(R.layout.popupwindow ,null)

        // Set content view of the popup window
        popupWindow.contentView = view

        // Set the width and height of the popup window
        popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT

        // Set focusable true to make the window focusable
        popupWindow.isFocusable = true

        // Show the popup window at the center of the activity
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        // Set a dismiss listener to close the popup window when clicked outside
        popupWindow.setOnDismissListener {
            // Perform any actions when the popup window is dismissed
        }
    }


}
