package com.example.guesswhosthat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.guesswhosthat.databinding.ActivitySignupBinding
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.guesswhosthat.Helpers.ErrorResponseHelper
import com.example.guesswhosthat.Helpers.NetworkUtil
import com.example.guesswhosthat.Models.ErrorResponse
import com.example.guesswhosthat.Models.UserLoginRequest
import com.example.guesswhosthat.Models.newUserRequest
import com.example.guesswhosthat.Services.APIManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.regex.Pattern


class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signup.setOnClickListener {
            if(inputValidation()){
                signUp()
            }else{
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Log in")
                    .setMessage("Check your information!")
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

    private fun inputValidation() : Boolean{
        val user = binding.usernameReg.text.toString().trim()
        val email = binding.usernameReg.text.toString().trim()
        val pass = binding.passwordReg.text.toString().trim()

        return if(!(user.isEmpty() || pass.isEmpty() || email.isEmpty())){
            val pattern: Pattern = Patterns.EMAIL_ADDRESS
            pattern.matcher(email).matches()
        }else{
            false
        }

    }

    private fun signUp(){
        if(NetworkUtil.isOnline(this)){
            CoroutineScope(Dispatchers.IO).launch{
                val call = APIManager().getApiObj(this@Signup).newUser(newUserRequest(
                    binding.usernameReg.text.toString(),
                    binding.emailReg.text.toString(),
                    binding.passwordReg.text.toString()
                ))
                val body = call.body()
                runOnUiThread {
                    if(call.isSuccessful){
                        if(body!=null){
                            if(call.code()==200){
                                AlertDialog.Builder(this@Signup)
                                    .setTitle("Sign Up")
                                    .setMessage("complete your registration by verifying your account by email.!!").create().show()
                            }
                            Toast.makeText(this@Signup, body.message, Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        try {
                            Toast.makeText(this@Signup, ErrorResponseHelper.getErrorMessage(call.errorBody()), Toast.LENGTH_LONG).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@Signup, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }else{
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_LONG).show()
        }
    }
}