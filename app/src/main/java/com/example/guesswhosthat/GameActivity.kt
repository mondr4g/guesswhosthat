package com.example.guesswhosthat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.os.SystemClock
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Chronometer.OnChronometerTickListener
import com.example.guesswhosthat.Helpers.*
import com.example.guesswhosthat.Models.GameBeginResponse
import com.example.guesswhosthat.Models.GameInfoResponse
import com.example.guesswhosthat.Session.LoginPref
import okhttp3.ResponseBody
import java.lang.Exception
import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import android.view.MotionEvent
import android.view.View
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import android.content.Intent
import android.widget.Button


class GameActivity : AppCompatActivity() {

    private lateinit var recView1 : RecyclerView
    private lateinit var recView2 : RecyclerView
    private lateinit var recView3 : RecyclerView
    private lateinit var recView4 : RecyclerView

    private lateinit var chrono: Chronometer

    private lateinit var characters : Array<Int>
    private var bot : Int = 0

    private lateinit var btn_guess1vsia : Button
    private var GUESS : Boolean = false

    private lateinit var btn_sendMsg : Button
    private lateinit var popupChat : PopupWindow

    //Loin preferences
    lateinit var session : LoginPref

    private var user : HashMap<String, String>? = null

    //Mi personaje
    private var myCharacter: Int = 0

    //personajes
    private lateinit var p : MutableList<PsjObj>

    //Juego 1vs1
    private lateinit var gameInfo1vs1: GameBeginResponse

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = LoginPref(this)
        user  = session.getUserDetails()
        val btn_pressed = intent.getStringExtra(BTN_PRESSED)

        when (btn_pressed) {
            "0" -> prepare1vs1()
            "1" -> prepare1vsFriends()
            "2" -> prepare1vsAI()
        }

    }

    var loadingDialog : LoadingDialog = LoadingDialog(this)

    fun prepare1vs1() {
        if(!NetworkUtil.isOnline(this)){
            Toast.makeText(this,"No tienes conexion a internet!!", Toast.LENGTH_SHORT).show()
            return
        }
        setContentView(R.layout.activity_board1vs1)

        btn_sendMsg = findViewById(R.id.send_msg)

        chrono = findViewById(R.id.chronos)
        chrono.setOnChronometerTickListener{
            changeData()
        }

        SocketHandler.mSocket!!.on("waiting"){ args->
            if (args[0] != null) {
                runOnUiThread{
                    val msj = args[0] as String
                    //loadingDialog.startLoadingDialog(msj)
                    Toast.makeText(this,msj, Toast.LENGTH_SHORT).show()

                }
            }
        }

        SocketHandler.mSocket!!.on("BeginGame"){args->
            if(args[0] != null){
                runOnUiThread {
                    try {
                        //Almacenar la info del juego
                        gameInfo1vs1  = ParseHelper.ParseGameInfo<GameBeginResponse>(args[0] as ResponseBody)
                        //Aqui acomodar los usuarios que llegan en en el response
                        characters = gameInfo1vs1.personajes
                        loadCards()
                        //Aqui iniciar
                        chrono.start()
                        loadingDialog.dismissDialog()
                        Toast.makeText(this,"El juego ha iniciado!!!!!!", Toast.LENGTH_SHORT).show()
                    }catch (e: Exception){
                        Toast.makeText(this,"Ocurrio Un error!", Toast.LENGTH_SHORT).show()
                        SocketHandler.mSocket!!.emit("new_game",user!!.get(LoginPref.KEY_USERID))
                    }
                }
            }
        }

        //SocketHandler.mSocket!!.emit("new_game",user!!.get(LoginPref.KEY_USERID))

        SocketHandler.mSocket!!.on("win_abandono"){ args->
            if(args[0]!=null){
                runOnUiThread {
                    val msj = args[0] as String
                    //loadingDialog.startLoadingDialog(msj)
                    Toast.makeText(this,msj, Toast.LENGTH_SHORT).show()
                    //Aqui realizar el avandono de la sala
                    var i : Intent = Intent(applicationContext, MenuActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
        }

        //Enviar respuesta de adivinacion

        //Falta cuando gana

        //cuando pierde

        //enviar mensaje, va en el onclick del boton

        //recepcion de mensaje

        //enviar abandono

        SocketHandler.mSocket!!.emit("new_game",user!!.get(LoginPref.KEY_USERID))

        btn_sendMsg.setOnClickListener {
            showPopupChat()
        }
    }

    fun prepare1vsFriends() {
        setContentView(R.layout.activity_board1vs1)
        chrono = findViewById(R.id.chronos)

    }

    fun prepare1vsAI() {
        generateCharacters()
        loadCards()
    }
/*
    @Override
    override fun onStart(){
        super.onStart()
        chrono.start()
    }*/

    fun loadCards() {
        setContentView(R.layout.activity_board1vsai)

        recView1 = findViewById(R.id.rowP1)
        recView2 = findViewById(R.id.rowP2)
        recView3 = findViewById(R.id.rowP3)
        recView4 = findViewById(R.id.rowP4)

        chrono = findViewById(R.id.chronos)

        Characters.getCharacters(characters, applicationContext)


        p  = Characters.getPersonajes()

        //Toast.makeText(applicationContext, p[0].personaje.nombre, Toast.LENGTH_SHORT).show()

        val data1 =
            Array(6) { i -> Character(p[i].personaje.nombre,p[i].resourceId) }

        val data2 =
            Array(6) { i -> Character(p[i+6].personaje.nombre,p[i+6].resourceId) }

        val data3 =
            Array(6) { i -> Character(p[i+12].personaje.nombre,p[i+12].resourceId) }

        val data4 =
            Array(6) { i -> Character(p[i+18].personaje.nombre,p[i+18].resourceId) }


        val adapter1 = CardAdapter(data1) { }

        val adapter2 = CardAdapter(data2) { }

        val adapter3 = CardAdapter(data3) { }

        val adapter4 = CardAdapter(data4) { }

        recView1.setHasFixedSize(true)
        recView1.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recView1.adapter = adapter1

        recView2.setHasFixedSize(true)
        recView2.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recView2.adapter = adapter2

        recView3.setHasFixedSize(true)
        recView3.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recView3.adapter = adapter3

        recView4.setHasFixedSize(true)
        recView4.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recView4.adapter = adapter4

        chrono.setOnChronometerTickListener{
            changeData()
        }
    }

    fun generateCharacters() {
        val N : Int = 39
        var n1 = 0
        var n2 = 0

        do {
            n1 = (0..N).random()
            n2 = (0..N).random()
        } while((n2-n1)!=23)

        characters =
            Array(24) { i -> i+n1 }

        bot = (n1..n2).random()
    }

    fun changeData(){
        val time = SystemClock.elapsedRealtime() - chrono.base
        val h = (time / 3600000).toInt()
        val m = (time - h * 3600000).toInt() / 60000
        val s = (time - h * 3600000 - m * 60000).toInt() / 1000
        val hh = if (h < 10) "0$h" else h.toString() + ""
        val mm = if (m < 10) "0$m" else m.toString() + ""
        val ss = if (s < 10) "0$s" else s.toString() + ""
        chrono.text = "$hh:$mm:$ss"
    }

    fun showPopupChat() {
        // Inflate layout
        var inflater : LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var popupView = inflater.inflate(R.layout.popup_chat,null)

        var recViewChat : RecyclerView = popupView.findViewById(R.id.chat)

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val dataMsg =
            Array(10) { i ->
            if (i % 2 == 0)
                UserMsg("Emisor", 1,currentDate)
            else
                UserMsg("Remitente", 0,currentDate)
            }

        val adapterMsg = UserMsgAdapter(dataMsg) { }

        recViewChat.setHasFixedSize(true)
        recViewChat.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recViewChat.adapter = adapterMsg

        recViewChat.scrollToPosition(dataMsg.size - 1)

        // Create popup chat
        var w : Int = LinearLayout.LayoutParams.WRAP_CONTENT
        var h : Int = LinearLayout.LayoutParams.WRAP_CONTENT
        var focusable : Boolean = true
        popupChat = PopupWindow(popupView,w,h,focusable)

        var view : LinearLayout = findViewById(R.id.board)
        popupChat.showAtLocation(view, Gravity.CENTER, 0, 0);
        // dismiss the popup window when touched
        popupView.setOnTouchListener { v, event ->
            popupChat.dismiss()
            true
        }
    }

    @Override
    override fun onBackPressed() {
        super.onBackPressed()
        Characters.emptyCharsList()
    }
}