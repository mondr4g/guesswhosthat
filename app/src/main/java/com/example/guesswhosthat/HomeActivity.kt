package com.example.guesswhosthat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    lateinit var login : Button
    lateinit var signup : Button

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        login = findViewById(R.id.btn_login)
        signup = findViewById(R.id.btn_signup)

        login.setOnClickListener { userOption("login")}
        signup.setOnClickListener { userOption("signup")}
    }

    fun userOption(option : String) {
        if (option == "login") {
            var intent1 = Intent(this, Login::class.java)
            startActivity(intent1)
        }
        else if (option == "signup") {
            var intent2 = Intent(this, Signup::class.java)
            startActivity(intent2)
        }
    }
}