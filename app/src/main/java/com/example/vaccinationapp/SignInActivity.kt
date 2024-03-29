package com.example.vaccinationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.vaccinationapp.phpAdmin.DBConnection
import com.example.vaccinationapp.phpAdmin.DBQueries
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.MessageDigest

class SignInActivity : AppCompatActivity() {

    private lateinit var goToSignUpButton: Button
    private lateinit var breakInButton: Button
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var logToApp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        goToSignUpButton = findViewById(R.id.go_to_sign_up_button)
        breakInButton = findViewById(R.id.break_in_button)
        inputEmail = findViewById(R.id.email_edit)
        inputPassword = findViewById(R.id.password_edit)
        logToApp = findViewById(R.id.sign_in_button)


        goToSignInActivity()
        logIntoApp()

    }


    private fun logIntoApp(){
        logToApp.setOnClickListener{
            logInRegisteredUser()
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

//    private fun logInRegisteredUser() {
//        // validating from validateLoginDetails()
//        if (validateLoginDetails()) {
//            val email = inputEmail.text.toString().trim()
//            val password = inputPassword.text.toString().trim()
//
//            // getting email and password from Firebase
//            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        // Go to some Activity !!
//                        finish()
//                    } else {
//                        makeToast(task.exception?.message.toString(), true)
//                    }
//                }
//        }
//    }

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
                    //openNextActivity()
                } else {
                    showToast("User not found")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Function to open the next activity testLogin replace with real class
//    private fun openNextActivity() {
//        val intent = Intent(this, testLogin::class.java)
//        startActivity(intent)
//    }

    // Function to display a toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun hashData(data: String): String {
        val bytes = data.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.toHexString()
    }

    fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

    private fun makeToast(toast: String, errorMessage: Boolean) {
        Toast.makeText(this@SignInActivity, toast, Toast.LENGTH_LONG).show()
    }

}
