package com.example.guesswhosthat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Signup : AppCompatActivity() {

    lateinit var username : EditText
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var btnSignup : Button

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        username = findViewById(R.id.username_reg)
        email = findViewById(R.id.email_reg)
        password = findViewById(R.id.password_reg)
        btnSignup = findViewById(R.id.signup)

        // On btn signup click event
        btnSignup.setOnClickListener {
            // Look for empty inputs
            if (this.inputValidation()) {
                // Send data to the DB
                val status : Boolean = true
                // Check for succesful registration
                if (status) {
                    Toast.makeText(applicationContext, "Succesful registration", Toast.LENGTH_LONG).show()
                    var i = Intent(applicationContext, Login::class.java)
                    startActivity(i)
                    finish()
                }
                else {
                    // Error message fo failed registration
                    val dialog = AlertDialog.Builder(this)
                        .setTitle("Sign Up")
                        .setMessage("Something went wrong with the registration. Please try again!")
                        .setNegativeButton("Cancel") { view, _ ->
                            finish()
                        }
                        .setPositiveButton("Try Again") { view, _ ->
                        }
                        .setCancelable(false)
                        .create()

                    dialog.show()
                }
            }
            else {
                // Error message to complete all inputs
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Sign Up")
                    .setMessage("Complete all the information for the registration")
                    .setNegativeButton("Cancel") { view, _ ->
                        finish()
                    }
                    .setPositiveButton("Accept") { view, _ ->
                    }
                    .setCancelable(false)
                    .create()

                dialog.show()
            }
        }
    }

    // Check if there is any empty input
    private fun inputValidation() : Boolean {
        val user = username.text.toString().trim()
        val eml = email.text.toString().trim()
        val passwd = password.text.toString().trim()

        return !(user.isEmpty() || passwd.isEmpty() || eml.isEmpty())
    }
}