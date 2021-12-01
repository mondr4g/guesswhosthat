package com.example.guesswhosthat

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.guesswhosthat.Helpers.ErrorResponseHelper
import com.example.guesswhosthat.Helpers.NetworkUtil
import com.example.guesswhosthat.Models.ErrorResponse
import com.example.guesswhosthat.Models.UserLoginRequest
import com.example.guesswhosthat.Models.UserLoginResponse
import com.example.guesswhosthat.Services.APIManager
import com.example.guesswhosthat.Session.LoginPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception


class Login : AppCompatActivity() {

    lateinit var btnLogin : Button
    lateinit var username : EditText
    lateinit var  password : EditText

    lateinit var session : LoginPref

    lateinit var btnForgotPassword : TextView

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = LoginPref(this)

        btnLogin = findViewById(R.id.login)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        btnForgotPassword = findViewById(R.id.textView2)

        btnLogin.setOnClickListener {
            if (this.inputValidation()) {
                loginValidation()
            }
            else {
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

        btnForgotPassword.setOnClickListener {
            var i : Intent = Intent(applicationContext, ForgotPasswdActivity::class.java)
            startActivity(i)
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
        if(NetworkUtil.isOnline(this@Login)){
            CoroutineScope(Dispatchers.IO).launch {
                //val call = APIManager().getApiObj().postToApi<UserLoginRequest, UserLoginResponse>("users/login",UserLoginRequest(username.toString(), password.toString()))
                //AAAAAA
                val call = APIManager().getApiObj(this@Login).userLogin(UserLoginRequest(username.text.toString(), password.text.toString()))
                val body = call.body()
                runOnUiThread {
                    if(call.isSuccessful){
                        if(body!=null){
                            Toast.makeText(this@Login, body.message, Toast.LENGTH_SHORT).show()
                            session.createLogginSession(username.text.toString(),password.text.toString(),
                                body.id.toString()
                            )
                            var i : Intent = Intent(applicationContext, MenuActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                    }else{
                        try {
                            Toast.makeText(this@Login, ErrorResponseHelper.getErrorMessage(call.errorBody()), Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@Login, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }else{
            session.createLogginSession(username.text.toString(),password.text.toString(),
               ""
            )
            var i : Intent = Intent(applicationContext, MenuActivity::class.java)
            startActivity(i)
            finish()
            Toast.makeText(this@Login, "No internet connection!", Toast.LENGTH_LONG).show()

        }

    }

    @Override
    override fun onBackPressed() {
        super.onBackPressed()
        var i : Intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(i)
        finish()
    }
}