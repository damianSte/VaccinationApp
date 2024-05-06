package com.example.vaccinationapp.UserAvtivities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vaccinationapp.Functional.BarHandler
import com.example.vaccinationapp.R
import com.example.vaccinationapp.VaccineControl.AddPastVaccinesActivity
import com.example.vaccinationapp.phpAdmin.DBConnection
import com.example.vaccinationapp.phpAdmin.DBQueries
import com.example.vaccinationapp.phpAdmin.DataClasses.AddVaccineDataClass
import com.example.vaccinationapp.phpAdmin.DataClasses.UserProfileDataClass
import com.example.vaccinationapp.phpAdmin.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserProfile : BarHandler() {

    private lateinit var userName: EditText
    private lateinit var userPesel: EditText
    private lateinit var userPhoneNumber: EditText
    private lateinit var userDateOfBirth: EditText
    private lateinit var saveButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        openActivity(R.id.bottom_profile)

        userName = findViewById(R.id.editTextName)
        userPesel = findViewById(R.id.editTextPesel)
        userPhoneNumber = findViewById(R.id.editTextPhone)
        userDateOfBirth = findViewById(R.id.editTextBirth)
        saveButton = findViewById(R.id.buttonSaveProfile)

        saveButton.setOnClickListener {
            insertUserData()
        }

    }

    private fun insertUserData() {

        val name = userName.text.toString()
        val pesel = userPesel.text.toString()
        val phoneNumber = userPhoneNumber.text.toString()
        val birth = userDateOfBirth.text.toString()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val userId = UserData.getUserId()

                if (userId != null) {
                    val connection = DBConnection.getConnection()
                    val dbQueries = DBQueries(connection)

                    val newUser = UserProfileDataClass(userId, pesel, phoneNumber, birth, name)
                    dbQueries.insertProfile(newUser)

                    connection.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}