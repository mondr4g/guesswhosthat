package com.example.guesswhosthat

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.guesswhosthat.Helpers.SocketHandler
import com.example.guesswhosthat.Session.LoginPref
import io.socket.client.Socket


class MenuActivity : AppCompatActivity() {
    private lateinit var username : TextView
    private lateinit var password : TextView
    private lateinit var btnLogout : Button

    private lateinit var btnSocketConn: Button
    private lateinit var btnSocketDisc: Button

    private var mSocket: Socket? = null

    lateinit var session : LoginPref

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        session = LoginPref(this)

        username = findViewById(R.id.tvUsername)
        password = findViewById(R.id.tvPassword)
        btnLogout = findViewById(R.id.btn_logout)

        btnSocketConn = findViewById(R.id.btnSocketConn)
        btnSocketDisc = findViewById(R.id.btnSocketDisc)

        session.checkLogin()

        var user : HashMap<String, String> = session.getUserDetails()

        username.text = user.get(LoginPref.KEY_USERNAME)
        password.text = user.get(LoginPref.KEY_PASSWORD)




        btnLogout.setOnClickListener {
            session.LogoutUser()
            finish()
        }

        btnSocketConn.setOnClickListener {
            if(!SocketHandler.isConnected()){
                // Con estas lineas conectaremos al servidor
                SocketHandler.setSocket()
                SocketHandler.establishConnection()

                mSocket = SocketHandler.getSocket()
            }

        }

        btnSocketDisc.setOnClickListener {
            if(SocketHandler.isConnected()){
               SocketHandler.closeConnection()
            }
        }
    }
}