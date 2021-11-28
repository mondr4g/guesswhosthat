package com.example.guesswhosthat.Session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.guesswhosthat.HomeActivity

class LoginPref {

    lateinit var pref : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    lateinit var context : Context

    var PRIVATEMODE : Int = 0

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATEMODE)
        editor = pref.edit()
    }

    companion object {
        val PREF_NAME = "Login_Preference"
        val IS_LOGIN = "isLoggedin"
        val KEY_USERNAME = "username"
        val KEY_PASSWORD = "password"
        val KEY_USERID = "user_id"
    }

    fun createLogginSession(username: String, password : String, userid : String ) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PASSWORD, password)
        editor.putString(KEY_USERID, userid)
        editor.commit()
    }

    fun checkLogin() {
        if (!this.isLoggedin()) {
            var i : Intent = Intent(context, HomeActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }
    }

    fun getUserDetails() : HashMap<String, String> {
        var user : Map<String, String> = HashMap<String, String>()
        (user as HashMap).put(KEY_USERNAME, pref.getString(KEY_USERNAME, null)!!)
        (user as HashMap).put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null)!!)
        return user
    }

    fun LogoutUser() {
        editor.clear()
        editor.commit()
        var i : Intent = Intent(context, HomeActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
    }

    fun isLoggedin() : Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }
}