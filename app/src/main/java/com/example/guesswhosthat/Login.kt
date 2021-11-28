package com.example.guesswhosthat

import android.app.appsearch.AppSearchSession
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.guesswhosthat.Session.LoginPref

class Login : AppCompatActivity() {

    lateinit var btnLogin : Button
    lateinit var username : EditText
    lateinit var  password : EditText

    lateinit var session: LoginPref

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = LoginPref(this)

        btnLogin = findViewById(R.id.login)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        btnLogin.setOnClickListener {
            if (this.inputValidation()) {
                //this.loginValidation()
                // Create new session for the user
                session.createLogginSession(username.text.toString(),password.text.toString())
                // Start main menu activity
                var i : Intent = Intent(applicationContext, MenuActivity::class.java)
                startActivity(i)
                finish()
            }
            else {
                // Error message to complete all inputs
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Log in")
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
        val user = username.text.toString().trim()
        val passwd = password.text.toString().trim()

        return !(user.isEmpty() || passwd.isEmpty())
    }

    // Login Validation
    private fun loginValidation() {

    }
}