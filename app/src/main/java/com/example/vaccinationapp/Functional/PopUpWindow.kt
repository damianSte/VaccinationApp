package com.example.vaccinationapp.Functional

import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccinationapp.R

class PopUpWindow : AppCompatActivity() {

    lateinit var getName: TextView
    lateinit var getDate: TextView
    lateinit var getHour: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popupwindow)

        getDate = findViewById(R.id.getVaccineDate)
        getHour = findViewById(R.id.getVaccineHour)
        getName = findViewById(R.id.getVaccineName)

    }

    private fun showPopupWindow(vaccineName: String, date: String, hour:String) {
        getDate.text = date
        getHour.text = hour
        getName.text = vaccineName

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