package com.example.guesswhosthat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.graphics.Typeface
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.Toast
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
import android.util.Log
import android.widget.Button
import com.example.guesswhosthat.Helpers.ParseHelper.parseChido
import com.example.guesswhosthat.Models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.beans.IndexedPropertyChangeEvent

class GameActivity : AppCompatActivity() {

    private lateinit var recView1 : RecyclerView
    private lateinit var recView2 : RecyclerView
    private lateinit var recView3 : RecyclerView
    private lateinit var recView4 : RecyclerView

    private lateinit var chrono: Chronometer

    private lateinit var btnMusik: Button
    private var muted: Boolean = false

    private lateinit var characters : Array<Int>
    private var bot : Int = 0

    private lateinit var popupPerson: Button

    private lateinit var btn_guess1vsia : Button
    private var GUESS : Boolean = false

    //pal boton de abandono
    private lateinit var btnAbandono : Button

    private lateinit var btn_sendMsg : Button
    private lateinit var popupChat : PopupWindow
    private lateinit var popupChar : PopupWindow
    private lateinit var message : EditText

    //Loin preferences
    lateinit var session : LoginPref

    private var user : HashMap<String, String>? = null

    //Mi personaje
    private var myCharacter: Int = 0

    //personajes
    private lateinit var p : MutableList<PsjObj>

    //Juego 1vs1
    private lateinit var gameInfo1vs1: GameBeginResponse

    //Para los mensajes del whatsapp
    private lateinit var mensajesDelWhats : ChatUpdateResponse

    //para identificar que bonon se presiono.
    private lateinit var btn_pressed: String

    companion object {
        lateinit var fa : Activity
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = LoginPref(this)
        user  = session.getUserDetails()
        val btn_pressed = intent.getStringExtra(BTN_PRESSED)

        val mIntent = Intent()
        mIntent.action="Battle"
        sendBroadcast(mIntent)

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

        fa = this

        btn_sendMsg = findViewById(R.id.send_msg)

        popupPerson = findViewById(R.id.btn_poperson)
        popupPerson.setOnClickListener { showPopupInfo() }

        message = findViewById(R.id.chat_1vs1)

        btnMusik = findViewById(R.id.btn_music1vs1)
        btnMusik.setOnClickListener {
            changeMusicState()
        }

        chrono = findViewById(R.id.chronos)
        chrono.setOnChronometerTickListener{
            changeData()
        }
        btnAbandono = findViewById(R.id.btnAbandonar)
        btnAbandono.setOnClickListener {
            leftGame()
        }
        SocketHandler.mSocket!!.on("waiting"){ args->
            if (args[0] != null) {
                runOnUiThread{
                    val msj = args[0] as String
                    loadingDialog.startLoadingDialog(msj)
                    Toast.makeText(this,msj, Toast.LENGTH_SHORT).show()
                }
            }
        }

        SocketHandler.mSocket!!.on("BeginGame"){args->
            if(args[0] != null){
                runOnUiThread {
                    try {
                        //Almacenar la info del juego
                        //gameInfo1vs1  = ParseHelper.ParseGameInfo<GameBeginResponse>(args[0] as ResponseBody)
                        val type = object : TypeToken<GameBeginResponse>() {}.type
                        gameInfo1vs1 = parseChido<GameBeginResponse>(json = args[0].toString(), typeToken = type)


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
        //realizar emicion de evento

        //cuando gana

        //cuando pierde

        //enviar mensaje, va en el onclick del boton
        //emitir evento: new_message
        /*Enviar lo siguiente
        data = {
            var emisor = data.emisor
            var receptor = data.receptor
            var gameId = data.gameId
            var msj = data.msj
        }
        * */

        //recepcion de mensaje
        SocketHandler.mSocket!!.on("message_recived"){args->
            /*data = args[0]
            * {
            * message: String,
            * history: [{
            *   emisor: String,
                receptor: String,
                msj: String,
                fecha: String
            * }]
            * }
            * */

            if(args[0]!=null){
                runOnUiThread {
                    try {
                        //Almacenar la info del juego
                        val type = object : TypeToken<ChatUpdateResponse>() {}.type
                        mensajesDelWhats = parseChido<ChatUpdateResponse>(json = args[0].toString(), typeToken = type)
                        showPopupChat()
                        //mensajesDelWhats = ParseHelper.ParseGameInfo<ChatUpdateResponse>(args[0] as ResponseBody)
                        //Aqui mostrar notificacion. o notificarle que llegaron los mensajes

                    }catch (e: Exception){
                        Toast.makeText(this,"No se han podido cargar los mensajes!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        //prueba de parametros
        SocketHandler.mSocket!!.on("respuesta_serv"){args->
            if(args[0]!=null){
                runOnUiThread {
                    try{
                        //AbandonoRequest
                        val type = object : TypeToken<AbandonoResponse>() {}.type
                        val result: AbandonoResponse = parseChido<AbandonoResponse>(json = args[0].toString(), typeToken = type)
                        //var a = ParseHelper.ParseJsonObject<AbandonoResponse>(args[0].toString())
                        Toast.makeText(this,result.toString(), Toast.LENGTH_SHORT).show()
                        //Aqui lanzar el evento de que gano o lo que sea sepa la vrg
                    }catch (e: Exception){
                        e.message?.let { Log.d("TAG", it) }

                        Toast.makeText(this,"No se han podido cargar los mensajes!", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

        SocketHandler.mSocket!!.emit("new_game",user!!.get(LoginPref.KEY_USERID))

        btn_sendMsg.setOnClickListener {
            showPopupChat()
        }
    }

    fun prepare1vsFriends() {
        setContentView(R.layout.activity_board1vs1)
        chrono = findViewById(R.id.chronos)
        btnMusik = findViewById(R.id.btn_music1vs1)

        btnMusik.setOnClickListener {
            changeMusicState()
        }
        chrono.setOnChronometerTickListener{
            changeData()
        }
        chrono.start()

        popupPerson = findViewById(R.id.btn_poperson)
        popupPerson.setOnClickListener { showPopupInfo() }

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

        btnMusik = findViewById(R.id.btn_music1vs1)

        chrono = findViewById(R.id.chronos)

        Questions.getPregs(applicationContext)
        Characters.getCharacters(characters, applicationContext, bot)
        generateCharacters()



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

        btnMusik.setOnClickListener {
            changeMusicState()
        }

        chrono.setOnChronometerTickListener{
            changeData()
        }
        chrono.start()
    }

    fun changeMusicState(){
        if(muted) {
            resume()
            muted = false
        }else {
            pause()
            muted = true
        }
    }

    fun pause(){
        val mIntent = Intent()
        mIntent.action="Pause"
        sendBroadcast(mIntent)
    }
    fun resume(){
        val mIntent = Intent()
        mIntent.action="Resume"
        sendBroadcast(mIntent)
    }

    /*@Override
    override fun onStart(){
        super.onStart()
        chrono.setOnChronometerTickListener{
            changeData()
        }
        chrono.start()
    }*/

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

    //funcion para EL BOTON DE ABANDONAR
    fun leftGame(){
        chrono.stop()
        var duracion = chrono.text.toString()
        when (btn_pressed) {
            "2" -> {

            }
            else->{
                //enviar abandono
                /*
                * var gameId = data.gameId
                var winnerId = data.winnerId
                var loserId = data.loserId
                var duration = data.duration
                * */
                //var getLos = getOppositeId()
                var userId = user!!.get(LoginPref.KEY_USERID).toString()
                var d = AbandonoRequest(
                    "aaaaaaa prra madre",
                    //getLos,
                    "Aqui voy perro",
                    userId,
                    duracion
                )
                //Asi se envia mierda por json
                var dataa = Json.encodeToJsonElement(d)
                SocketHandler.mSocket!!.emit("prueba_parametros", dataa)

            }
        }
    }

    private fun getOppositeId(): String {
        val usI = user!!.get(LoginPref.KEY_USERID)
        return if(usI == gameInfo1vs1.gameInfo.player1){
            gameInfo1vs1.gameInfo.player2
        }else{
            gameInfo1vs1.gameInfo.player1
        }
    }

    //para mensajes
    private fun sendMessage(){
        //recuperar el texto del input, cambiar
        var mensaje = message.text.toString()

        if (message == null) {
            return
        }

        //actualizar valores
        var id_emisor = user!!.get(LoginPref.KEY_USERID).toString()
        var id_recptor = "UN wey X"
        var id_game = "ID X"
        if(gameInfo1vs1!=null){
            id_recptor = getOppositeId()
            id_game = gameInfo1vs1.gameInfo.id
        }
        var datMessage = NewMessageRequest(
            emisor = id_emisor,
            receptor = id_recptor,
            gameId = id_game,
            msj = mensaje
        )

        val dataa = Json.encodeToString(datMessage)
        SocketHandler.mSocket!!.emit("new_message",dataa)
    }

    fun showPopupInfo(){
        // Inflate layout
        var inflater : LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var popupView = inflater.inflate(R.layout.popup_character,null)

        val pic: ImageView = popupView.findViewById(R.id.char_pic)
        val name: TextView = popupView.findViewById(R.id.pop_name)
        val gend: TextView = popupView.findViewById(R.id.pop_gen)
        val skin: TextView = popupView.findViewById(R.id.pop_tez)
        val acce: TextView = popupView.findViewById(R.id.pop_acc)
        val hcol: TextView = popupView.findViewById(R.id.pop_hairCol)
        val hsiz: TextView = popupView.findViewById(R.id.pop_hairSiz)
        val htpe: TextView = popupView.findViewById(R.id.pop_hairTpe)
        val ears: TextView = popupView.findViewById(R.id.pop_ears)
        val nose: TextView = popupView.findViewById(R.id.pop_nose)
        val lips: TextView = popupView.findViewById(R.id.pop_lips)
        val ecol: TextView = popupView.findViewById(R.id.pop_eyeCol)
        val etpe: TextView = popupView.findViewById(R.id.pop_eyeTpe)
        val brow: TextView = popupView.findViewById(R.id.pop_brows)
        val face: TextView = popupView.findViewById(R.id.pop_faceTpe)

        val player = Characters.getPlayerChar()

        pic.setImageResource(player.resourceId)
        name.text = player.personaje.nombre
        gend.text = "Genero: " + player.personaje.genero
        skin.text = "Tez: " + player.personaje.tez
        acce.text = "Accesorios: " + player.personaje.accesorios
        hcol.text = "Color de cabello: " + player.personaje.cabello.color
        hsiz.text = "Tamaño de cabello: " + player.personaje.cabello.tamaño
        htpe.text = "Tipo de cabello: " + player.personaje.cabello.tipo
        ears.text = "Orejas: " + player.personaje.rostro.orejas
        nose.text = "Tipo de nariz: " + player.personaje.rostro.nariz
        lips.text = "Tipo de labios: " + player.personaje.rostro.labios
        ecol.text = "Color de ojos: " + player.personaje.rostro.ojos.color
        etpe.text = "Ojos: " + player.personaje.rostro.ojos.tipo
        brow.text = "Cejas: " + player.personaje.rostro.cejas
        face.text = "Tipo de rostro: " + player.personaje.rostro.tipo

        // Create popup
        var w : Int = LinearLayout.LayoutParams.WRAP_CONTENT
        var h : Int = LinearLayout.LayoutParams.WRAP_CONTENT
        var focusable : Boolean = true
        popupChar = PopupWindow(popupView,w,h,focusable)

        var view : LinearLayout = findViewById(R.id.board)
        popupChar.showAtLocation(view, Gravity.CENTER, 0, 0);
        // dismiss the popup window when touched
        popupView.setOnTouchListener { v, event ->
            popupChar.dismiss()
            true
        }
    }

    fun showPopupChat() {
        // Inflate layout
        var inflater : LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var popupView = inflater.inflate(R.layout.popup_chat,null)

        var recViewChat : RecyclerView = popupView.findViewById(R.id.chat)

//        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
//        val currentDate = sdf.format(Date())

        val dataMsg =
            Array(mensajesDelWhats.history.size) { i ->  UserMsg(mensajesDelWhats.history[i].message, mensajesDelWhats.history[i].emisor, mensajesDelWhats.history[i].date) }

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

        val mIntent = Intent()
        mIntent.action="Menu"
        sendBroadcast(mIntent)

        chrono.stop()
    }

    override fun onPause(){
        super.onPause()
        pause()
    }

    override fun onResume() {
        super.onResume()
        resume()
    }

}