package com.example.vaccinationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

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

    }

    // Replace SomeActivity with actualActivity
//    private fun breakInNoLog(){
//        breakInButton.setOnClickListener{
//            val intent = Intent(this, SomeActivity::class.java)
//            startActivity(intent)
//        }
//  }

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

    private fun logInRegisteredUser() {
        // validating from validateLoginDetails()
        if (validateLoginDetails()) {
            val email = inputEmail.text.toString().trim()
            val password = inputPassword.text.toString().trim()

            // getting email and password from Firebase
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Go to some Activity !!
                        finish()
                    } else {
                        makeToast(task.exception?.message.toString(), true)
                    }
                }
        }
    }

    private fun makeToast(toast: String, errorMessage: Boolean) {
        Toast.makeText(this@SignInActivity, toast, Toast.LENGTH_LONG).show()
    }

}
