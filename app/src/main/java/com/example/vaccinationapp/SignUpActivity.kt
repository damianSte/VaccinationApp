package com.example.vaccinationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.vaccinationapp.firebase.FireStoreClass
import com.example.vaccinationapp.firebase.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {

    private lateinit var goToSignInButton: Button
    private lateinit var registerEmail: EditText
    private lateinit var registerPassword: EditText
    private lateinit var repeatPassword: EditText
    private lateinit var registerName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        goToSignInButton = findViewById(R.id.go_to_sign_in_button)
        registerEmail = findViewById(R.id.email_edit)
        registerPassword = findViewById(R.id.password_edit)
        repeatPassword = findViewById(R.id.repeat_password_edit)
        registerName = findViewById(R.id.name_edit)

        goToSignIn()

        val registerButton = findViewById<Button>(R.id.sign_up_button)
        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun goToSignIn() {
        goToSignInButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {
        // email used for registration
        val email: String = registerEmail.text.toString().trim(' ')

        // password chosen in registration
        val password: String = registerPassword.text.toString().trim(' ')

        // name according to registered Name
        val name: String = registerName.text.toString().trim(' ')

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        val user = User(
                            "ID",
                            name,
                            true,
                            email,
                        )
                        FireStoreClass().registerUserFS(this@SignUpActivity, user)
                        // FirebaseAuth.getInstance().signOut() // Remove this line
                        finish()

                    } else {
                        Toast.makeText(
                            this@SignUpActivity, resources.getString(R.string.register_failed),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )

    }

    fun userRegistrationSuccess() {
        Toast.makeText(
            this@SignUpActivity, resources.getString(R.string.register_success),
            Toast.LENGTH_LONG
        ).show()
    }
}