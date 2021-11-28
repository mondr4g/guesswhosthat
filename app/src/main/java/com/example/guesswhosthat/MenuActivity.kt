package com.example.guesswhosthat

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.guesswhosthat.Session.LoginPref

class MenuActivity : AppCompatActivity() {
    private lateinit var username : TextView
    private lateinit var password : TextView
    private lateinit var btnLogout : Button

    lateinit var session : LoginPref

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        session = LoginPref(this)

        username = findViewById(R.id.tvUsername)
        //password = findViewById(R.id.tvPassword)
        btnLogout = findViewById(R.id.btn_logout)

        // Check if user is already logged in to skip login activity
        session.checkLogin()

        // Obtained user details from the shared preferences
        var user : HashMap<String, String> = session.getUserDetails()

        // Welcome message
        username.text = "Welcome back " + user.get(LoginPref.KEY_USERNAME)
        //password.text = user.get(LoginPref.KEY_PASSWORD)

        // Log out from the actual session
        btnLogout.setOnClickListener {
            session.LogoutUser()
            finish()
        }
    }
}