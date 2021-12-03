package com.example.guesswhosthat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guesswhosthat.Helpers.ErrorResponseHelper
import com.example.guesswhosthat.Helpers.NetworkUtil
import com.example.guesswhosthat.Helpers.SocketHandler
import com.example.guesswhosthat.Services.APIManager
import com.example.guesswhosthat.Session.LoginPref
import com.example.guesswhosthat.bkgndmusic.MusicService
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

const val BTN_PRESSED = "com.example.guesswhosthat.BTNPRESSED"

class MenuActivity : AppCompatActivity() {
    private lateinit var username : TextView
    private lateinit var btnLogout : Button

//    private lateinit var btnSocketConn: Button
//    private lateinit var btnSocketDisc: Button

    private lateinit var btn1vs1 : Button
    private lateinit var btnFriends : Button
    private lateinit var btn1vsAI : Button

    private var mSocket: Socket? = null

    lateinit var session : LoginPref

    private var user : HashMap<String, String>? = null

    private lateinit var popupProfile : PopupWindow
    private lateinit var btnProfile : Button
    private lateinit var btnCloseProfile : Button

    companion object {
        lateinit var fa : Activity
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        fa = this

        session = LoginPref(this)

        username = findViewById(R.id.tvUsername)
        btnLogout = findViewById(R.id.btn_logout)

//        btnSocketConn = findViewById(R.id.btnSocketConn)
//        btnSocketDisc = findViewById(R.id.btnSocketDisc)

        btn1vs1 = findViewById(R.id.btn_1vs1)
        btnFriends = findViewById(R.id.btn_playFriends)
        btn1vsAI = findViewById(R.id.btn_1vsia)
        btnProfile = findViewById(R.id.profile)

        session.checkLogin()

        user  = session.getUserDetails()

        username.text = "Welcome " + user!!.get(LoginPref.KEY_USERNAME)

        val intent = Intent(applicationContext, MusicService::class.java)
        startService(intent)

        //Pa conectar a servidor de sockets
        if(NetworkUtil.isOnline(this)){
            connectSocket()
        }else{
            Toast.makeText(this, "Revisa tu conexion a internet!", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            if(NetworkUtil.isOnline(this)){
                disconnectSocket()
                CoroutineScope(Dispatchers.IO).launch{
                    val id = user!!.get(LoginPref.KEY_USERID)
                    if(id!=null){
                        val call = APIManager().getApiObj(this@MenuActivity).userLogout(id)
                        val body = call.body()
                        runOnUiThread {
                            if(call.isSuccessful){
                                if(body!=null){
                                    Toast.makeText(this@MenuActivity, body.message, Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                try {
                                    Toast.makeText(this@MenuActivity, ErrorResponseHelper.getErrorMessage(call.errorBody()), Toast.LENGTH_LONG).show()
                                } catch (e: Exception) {
                                    Toast.makeText(this@MenuActivity, e.message, Toast.LENGTH_LONG).show()
                                }
                            }
                        }

                    }
                }
            }
            session.LogoutUser()
            finish()
        }



        btn1vs1.setOnClickListener {
            var i : Intent = Intent(applicationContext, GameActivity::class.java).apply {
                putExtra(BTN_PRESSED, "0")
            }
            startActivity(i)
        }

        btnFriends.setOnClickListener {
            var i : Intent = Intent(applicationContext, WaitingRoomActivity::class.java).apply {
                putExtra(BTN_PRESSED, "1")
            }
            startActivity(i)
        }

        btn1vsAI.setOnClickListener {
            var i : Intent = Intent(applicationContext, GameActivity::class.java).apply {
                putExtra(BTN_PRESSED, "2")
            }
            startActivity(i)
        }

        btnProfile.setOnClickListener {
            showPopupProfile()
        }

    }

    private fun connectSocket(){
        if(!SocketHandler.isConnected()){
            // Con estas lineas conectaremos al servidor
            SocketHandler.setSocket(this)
            SocketHandler.establishConnection()

            mSocket = SocketHandler.getSocket()

            mSocket?.on("error_b"){ args->
                if (args[0] != null) {

                    runOnUiThread{
                        val msj = args[0] as String
                        Toast.makeText(this,msj, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            mSocket?.on("bye_msj"){ args->
                if (args[0] != null) {
                    runOnUiThread{
                        val msj = args[0] as String
                        Toast.makeText(this,msj, Toast.LENGTH_SHORT).show()

                        if(SocketHandler.isConnected()){
                            SocketHandler.closeConnection()
                        }
                    }
                }

            }
            //aqui actualizar pa recoger los usuarios disponibles
            mSocket?.on("users_avaliables"){ args->
                if (args[0] != null) {
                    //val msj = args[0] as String
                    runOnUiThread{
                        val msj = args[0]
                        Toast.makeText(this,"Si llego este wey", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            mSocket?.emit("new_user",user!!.get(LoginPref.KEY_USERID))
        }

    }

    fun disconnectSocket() {
        SocketHandler.mSocket?.emit("user_off",user!!.get(LoginPref.KEY_USERID) )
    }

    fun showPopupProfile() {
        // Inflate layout
        var inflater : LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var popupView = inflater.inflate(R.layout.popup_profile,null)

        // Create popup chat
        var w : Int = LinearLayout.LayoutParams.WRAP_CONTENT
        var h : Int = LinearLayout.LayoutParams.WRAP_CONTENT
        var focusable : Boolean = true

        var user_name : TextView = popupView.findViewById(R.id.user_name)
        var user_id : TextView = popupView.findViewById(R.id.user_id)

        user_name.text = user!!.get(LoginPref.KEY_USERNAME)
        user_id.text = user!!.get(LoginPref.KEY_USERID)

        popupProfile = PopupWindow(popupView,w,h,focusable)

        var view : ConstraintLayout = findViewById(R.id.menu)
        popupProfile.showAtLocation(view, Gravity.CENTER, 0, 0)

        btnCloseProfile = popupView.findViewById(R.id.btn_closeProfile)

        // dismiss the popup window when touched
        btnCloseProfile.setOnClickListener {
            popupProfile.dismiss()
            true
        }
    }


    // Exit Application
    @Override
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}