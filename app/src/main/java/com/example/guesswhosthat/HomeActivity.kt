package com.example.guesswhosthat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.guesswhosthat.Session.LoginPref

class HomeActivity : AppCompatActivity() {

    lateinit var login : Button
    lateinit var signup : Button

    lateinit var session : LoginPref

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        session = LoginPref(this)

        if (session.isLoggedin()) {
            var i : Intent = Intent(applicationContext, MenuActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
            finish()
        }

        login = findViewById(R.id.btn_login)
        signup = findViewById(R.id.btn_signup)

        login.setOnClickListener { userOption("login")}
        signup.setOnClickListener { userOption("signup")}
    }

    fun userOption(option : String) {
        if (option == "login") {
            var intent1 = Intent(applicationContext, Login::class.java)
            startActivity(intent1)
            finish()
        }
        else if (option == "signup") {
            var intent2 = Intent(applicationContext, Signup::class.java)
            startActivity(intent2)
            finish()
        }
    }
}