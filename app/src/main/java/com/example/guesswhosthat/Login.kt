package com.example.guesswhosthat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    lateinit var btnLogin : Button
    lateinit var username : EditText
    lateinit var  password : EditText

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.login)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        btnLogin.setOnClickListener {
            if (this.inputValidation()) {
                this.loginValidation()
            }
            else {
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Login")
                    .setMessage("Complete all the information")
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

    // Check empty inputs
    private fun inputValidation() : Boolean {
        val user = username.text.toString()
        val passwd = password.text.toString()

        return !(user == "" || passwd == "")
    }

    // Login Validation
    private fun loginValidation() {

    }
}