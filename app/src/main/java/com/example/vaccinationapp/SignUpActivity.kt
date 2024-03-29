package com.example.vaccinationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.example.vaccinationapp.phpAdmin.DBConnection
import com.example.vaccinationapp.phpAdmin.DBQueries
import com.example.vaccinationapp.phpAdmin.LogInDataClass
import kotlinx.coroutines.DelicateCoroutinesApi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.util.UUID


open class SignUpActivity : AppCompatActivity() {

    private lateinit var goToSignInButton: Button
    private lateinit var registerEmail: EditText
    private lateinit var registerPassword: EditText
    private lateinit var repeatPassword: EditText
    private lateinit var registerName: EditText
    private lateinit var registerButton: Button
    private lateinit var testButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        goToSignInButton = findViewById(R.id.go_to_sign_in_button)
        registerEmail = findViewById(R.id.email_edit)
        registerPassword = findViewById(R.id.password_edit)
        repeatPassword = findViewById(R.id.repeat_password_edit)
        registerName = findViewById(R.id.name_edit)
        registerButton = findViewById(R.id.sign_up_button)
        testButton = findViewById(R.id.TestButton)

        goToSignIn()
        registerNewUserButton()

    }

    private fun registerNewUserButton() {
        registerButton.setOnClickListener {
            addUserToPhp()
        }
    }

    private fun goToSignIn() {
        goToSignInButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateEmail(email: String): Boolean {
        val emailPatter = Patterns.EMAIL_ADDRESS
        return emailPatter.matcher(email).matches()
    }

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

    //    private fun registerUser() {
//        // email used for registration
//        val email: String = registerEmail.text.toString().trim(' ')
//
//        // password chosen in registration
//        val password: String = registerPassword.text.toString().trim(' ')
//
//        // name according to registered Name
//        val name: String = registerName.text.toString().trim(' ')
//
//        if (registrationData()) {
//            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(
//                    OnCompleteListener<AuthResult> { task ->
//                        if (task.isSuccessful) {
//                            val firebaseUser: FirebaseUser = task.result!!.user!!
//                            makeToast("Registration successful, user Id ${firebaseUser.uid}", false)
//
//                            val user = User(
//                                "ID",
//                                name,
//                                true,
//                                email,
//                            )
//
//                            FireStoreClass().registerUserFS(this@SignUpActivity, user)
//                            finish()
//
//                        } else {
//                            Toast.makeText(
//                                this@SignUpActivity, resources.getString(R.string.register_failed),
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//                )
//        }
//
//    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun addUserToPhp() {
        val email: String = registerEmail.text.toString().trim()
        val password: String = registerPassword.text.toString().trim()
        val userId = UUID.randomUUID().toString()

        // Launch a coroutine to perform database operations asynchronously
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = DBConnection.getConnection()
                val dbQueries = DBQueries(connection)

                val newUser = LogInDataClass(
                    userId,
                    email,
                    password
                )

                val isSuccessful = dbQueries.insertUser(newUser)

                if (isSuccessful) {
                    makeToast("Registration Successful userId: $userId ", false)
                    connection.close()
                } else {
                    makeToast("Unable to Add User", false)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //
//private fun addUserToPhp() {
    //        val email: String = registerEmail.text.toString().trim()
//        val password: String = registerPassword.text.toString().trim()
//
//        if (registrationData()) {
//            makeToast("we are here", false)
//            // Generate user ID
//            val userId = generateUserId()
//
//            // Assuming DBConnection.getConnection() returns a valid database connection
//            val connection = DBConnection.getConnection()
//            val dbQueries = DBQueries(connection)
//            val newUser = LogInDataClass(userId, email, password)
//
//            // Insert user data into the database
//            val isSuccess = dbQueries.insertUser(newUser)
//            connection.close()
//
//            if (isSuccess) {
//                // User registration successful
//                makeToast("Registration successful, user Id $userId", false)
//            } else {
//                // User registration failed
//                Toast.makeText(
//                    this@SignUpActivity,
//                    resources.getString(R.string.register_failed),
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
//    }
    private fun generateUserId(): String {
        return UUID.randomUUID().toString()
    }


    fun userRegistrationSuccess() {
        Toast.makeText(
            this@SignUpActivity, resources.getString(R.string.register_success),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun makeToast(toast: String, errorMessage: Boolean) {
        Toast.makeText(this@SignUpActivity, toast, Toast.LENGTH_LONG).show()
    }

}