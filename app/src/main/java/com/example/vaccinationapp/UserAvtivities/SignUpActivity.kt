package com.example.vaccinationapp.UserAvtivities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.vaccinationapp.Functional.HashClass
import com.example.vaccinationapp.R

import com.example.vaccinationapp.phpAdmin.DBConnection
import com.example.vaccinationapp.phpAdmin.DBQueries
import com.example.vaccinationapp.phpAdmin.DataClasses.SignUpDataClass
import kotlinx.coroutines.DelicateCoroutinesApi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.util.UUID

/**
 * Activity for user sign-up
 */
open class SignUpActivity : HashClass() {

    // Button to navigate to sign n activity
    private lateinit var goToSignInButton: Button
    // EditText for inputting Email
    private lateinit var registerEmail: EditText
    // EditText for inputting password
    private lateinit var registerPassword: EditText
    // EditText for repeating the password
    private lateinit var repeatPassword: EditText
    // EditText fot inputting user name
    private lateinit var registerName: EditText
    // Button to register new user
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initializes views
        goToSignInButton = findViewById(R.id.go_to_sign_in_button)
        registerEmail = findViewById(R.id.email_edit)
        registerPassword = findViewById(R.id.password_edit)
        repeatPassword = findViewById(R.id.repeat_password_edit)
        registerName = findViewById(R.id.name_edit)
        registerButton = findViewById(R.id.sign_up_button)

        // function set on click
        goToSignIn()
        registerNewUserButton()


    }

    /**
     * Sets up the click listener for the register button
     */
    private fun registerNewUserButton() {
        registerButton.setOnClickListener {
            addUserToPhp()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Sets up the click listener for the sign-in button
     */
    private fun goToSignIn() {
        goToSignInButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Validates the registration data entered by User
     */
    private fun registrationData(): Boolean {
        val email = registerEmail.text.toString().trim()
        val password = registerPassword.text.toString().trim()
        val repeatPassword = repeatPassword.text.toString().trim()

        return when {
            TextUtils.isEmpty(email) -> {
                makeToast(resources.getString(R.string.invalid_email), true)
                false
            }

            !validateEmail(email) -> {
                makeToast(resources.getString(R.string.invalid_email), true)
                false
            }

            TextUtils.isEmpty(password) -> {
                makeToast(resources.getString(R.string.pass_regulation), true)
                false
            }

            TextUtils.isEmpty(repeatPassword) -> {
                if (repeatPassword != password) {
                    makeToast(resources.getString(R.string.pass_not_the_same), true)
                }
                false
            }

            password != repeatPassword -> {
                makeToast(resources.getString(R.string.pass_not_the_same), true)
                false
            }

            else -> true
        }
    }

    /**
     * Adds a new user to the database.
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun addUserToPhp() {
        val email: String = registerEmail.text.toString().trim()
        val password: String = registerPassword.text.toString().trim()
        val userId = generateUserId()

        if (registrationData()) {
            // Launch a coroutine to perform database operations asynchronously
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val connection = DBConnection.getConnection()
                    val dbQueries = DBQueries(connection)

                    val newUser = SignUpDataClass(
                        userId,
                        hashData(email),
                        hashData(password)
                    )
                    val isSuccessful = dbQueries.insertUser(newUser)
                    if (isSuccessful) {
                        userRegistrationSuccess()
                        connection.close()
                    } else {
                        makeToast("Unable to Add User", false)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Validates the email format
     */
    private fun validateEmail(email: String): Boolean {
        val emailPatter = Patterns.EMAIL_ADDRESS
        return emailPatter.matcher(email).matches()
    }


    private fun generateUserId(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * Generates a unique user ID.
     */
    private fun userRegistrationSuccess() {
        Toast.makeText(
            this@SignUpActivity, resources.getString(R.string.register_success),
            Toast.LENGTH_LONG
        ).show()
    }


    private fun makeToast(toast: String, errorMessage: Boolean) {
        Toast.makeText(this@SignUpActivity, toast, Toast.LENGTH_LONG).show()
    }

}