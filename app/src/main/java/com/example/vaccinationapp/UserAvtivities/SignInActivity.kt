package com.example.vaccinationapp.UserAvtivities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.vaccinationapp.Functional.HashClass
import com.example.vaccinationapp.R
import com.example.vaccinationapp.VaccineControl.MainActivity
import com.example.vaccinationapp.phpAdmin.DBConnection
import com.example.vaccinationapp.phpAdmin.DBQueries
import com.example.vaccinationapp.phpAdmin.UserData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Activity for user sign-in
 */
class SignInActivity : HashClass() {

    // Button to go to Sing up Activity
    private lateinit var goToSignUpButton: Button

    // EditTExt for inputting Email
    private lateinit var inputEmail: EditText

    // EditText fot inputting password
    private lateinit var inputPassword: EditText

    // Button to log in into app
    private lateinit var logToApp: Button

    // Button to skip log in activity
    private lateinit var breakLogToApp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Initialize views
        goToSignUpButton = findViewById(R.id.go_to_sign_up_button)
        inputEmail = findViewById(R.id.email_edit)
        inputPassword = findViewById(R.id.password_edit)
        logToApp = findViewById(R.id.sign_in_button)
        breakLogToApp = findViewById(R.id.break_in_button)

        // Set functions
        goToSignInActivity()
        logIntoApp()
        breakIntoApp()


    }

    /**
     * Sets up the click listener for the login button
     */
    private fun logIntoApp() {
        logToApp.setOnClickListener {
            logInRegisteredUser()
        }
    }

    /**
     * Sets up the click listener for the skip login button
     */
    private fun breakIntoApp() {
        breakLogToApp.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Sets up the click listener for the sign-up button
     */
    private fun goToSignInActivity() {
        goToSignUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Logs in a registered user.
     */
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
                    getUserProfileValues()
                }
                if (!userExists) {
                    makeToast("User not found", false)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Sends the user ID to UserData if login is successful
     */

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

    /**
     * Opens the main Activity after successful login
     */
    private fun openNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    /**
     * Displays a toast message.
     *
     * @param toast the message to be displayed
     * @param errorMessage flag indicating whether the message is an error message
     */
    private fun makeToast(toast: String, errorMessage: Boolean) {
        Toast.makeText(this@SignInActivity, toast, Toast.LENGTH_LONG).show()
    }

    private fun getUserProfileValues() {
        val  userId =  UserData.getUserId()

        try {
            val connection = DBConnection.getConnection()

            val dbQueries = DBQueries(connection)

            if (userId != null) {
                dbQueries.getUserProfile(userId)

                connection.close()

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
