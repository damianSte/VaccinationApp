package com.example.vaccinationapp.UserAvtivities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.vaccinationapp.Functional.BarHandler
import com.example.vaccinationapp.Functional.HashClass
import com.example.vaccinationapp.VaccineControl.PopUpWindow
import com.example.vaccinationapp.R
import com.example.vaccinationapp.VaccineControl.MainActivity
import com.example.vaccinationapp.phpAdmin.DBConnection
import com.example.vaccinationapp.phpAdmin.DBQueries
import com.example.vaccinationapp.phpAdmin.GetUserId
import com.example.vaccinationapp.phpAdmin.UserData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.SQLException

class SignInActivity : HashClass() {

    private lateinit var goToSignUpButton: Button
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var logToApp: Button
    private lateinit var breakLogToApp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        goToSignUpButton = findViewById(R.id.go_to_sign_up_button)
        inputEmail = findViewById(R.id.email_edit)
        inputPassword = findViewById(R.id.password_edit)
        logToApp = findViewById(R.id.sign_in_button)
        breakLogToApp = findViewById(R.id.break_in_button)


        goToSignInActivity()
        logIntoApp()
        breakIntoApp()



    }

    private fun logIntoApp() {
        logToApp.setOnClickListener {
            logInRegisteredUser()
        }
    }

    private fun breakIntoApp() {
        breakLogToApp.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToSignInActivity() {
        goToSignUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateLoginDetails(): Boolean {
        // getting email and password trimming spaces
        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString().trim()

        return when {
            TextUtils.isEmpty(email) -> {
                makeToast(resources.getString(R.string.wrong_log_in_email), true)
                false
            }

            TextUtils.isEmpty(password) -> {
                makeToast(resources.getString(R.string.wrong_log_in_password), true)
                false
            }

            else -> {
                true
            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun logInRegisteredUser() {

        val email = hashData(inputEmail.text.toString().trim())
        val password = hashData(inputPassword.text.toString().trim())

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = DBConnection.getConnection()
                val dbQueries = DBQueries(connection)

                val userExists = dbQueries.userExists(email, password)
                connection.close()

                if (userExists) {
                    openNextActivity()
                    sendId()
                }
                if(!userExists) {
                    makeToast("User not found", false)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun sendId() {
        val email = inputEmail.text.toString().trim()
        val emailHashed = hashData(email)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = DBConnection.getConnection()
                val dbQueries = DBQueries(connection)

                val userId = dbQueries.getUserId(emailHashed)

                connection.close()

                if (userId != null) {
                    UserData.setUserId(userId)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun openNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    private fun makeToast(toast: String, errorMessage: Boolean) {
        Toast.makeText(this@SignInActivity, toast, Toast.LENGTH_LONG).show()
    }


}
