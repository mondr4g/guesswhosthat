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
import android.content.ContentValues.TAG
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.guesswhosthat.Helpers.ParseHelper.parseChido
import com.example.guesswhosthat.Models.*
import com.example.guesswhosthat.Session.CustomWinDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.Socket
import io.socket.emitter.Emitter
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

    // Character 1vsAI
    private var bot : Int = 0
    // Character 1vs1 Receptor
    private var guessCharReceptor : Int = 0
    // Character 1vs1 Emisor
    private var guessCharEmisor : Int = 0
    // Character getCharacters()
    private var idChar : Int = 0

    private lateinit var popupPerson: Button

    private lateinit var clueBtn: Button
    private lateinit var popupClue: PopupWindow

    private lateinit var btn_guess1vsia : Button

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

    //pal socket
    private var mSocket: Socket? = null

    // Personaje a adivinar
    private lateinit var adivinarChar : PsjObj

    companion object {
        lateinit var fa : Activity
        var GUESS : Boolean = false
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = LoginPref(this)
        user  = session.getUserDetails()
        btn_pressed = intent.getStringExtra(BTN_PRESSED).toString()

        val mIntent = Intent()
        mIntent.action="Battle"
        sendBroadcast(mIntent)
        when (btn_pressed) {
            "0" -> prepare1vs1()
            "1" -> prepare1vsFriends()
            "2" -> prepare1vsAI()
        }



    }
/*
    var onWaiting = Emitter.Listener {

    }

    var onBeginGame= Emitter.Listener {
    }

    var onAbandono = Emitter.Listener {
    }

    var onMessageRecived = Emitter.Listener {
    }

    var onRespuestaServ = Emitter.Listener{
    }
*/

    var loadingDialog : LoadingDialog = LoadingDialog(this)
    var customResultDialog: CustomWinDialog = CustomWinDialog(this)

    fun prepare1vs1() {
        if(!NetworkUtil.isOnline(this)){
            Toast.makeText(this,"No tienes conexion a internet!!", Toast.LENGTH_SHORT).show()
            return
        }
        setContentView(R.layout.activity_board1vs1)

        fa = this

        /*
      * try {
      alertDialog.show()
  }
catch (WindowManager.BadTokenException e) {
      //use a log message
  }
      * */
        //SocketHandler.mSocket!!.on("waiting", onWaiting)
        //SocketHandler.mSocket!!.off("waiting")

        SocketHandler.mSocket!!.on("waiting"){ args->
            if (args[0] != null) {

                runOnUiThread{
                    val msj = args[0] as String
                    Toast.makeText(this,msj, Toast.LENGTH_SHORT).show()
                    loadingDialog.startLoadingDialog(msj, user!!.get(LoginPref.KEY_USERID).toString())

                    /*try{
                        loadingDialog.startLoadingDialog(msj, user!!.get(LoginPref.KEY_USERID).toString())
                        Toast.makeText(this,msj, Toast.LENGTH_SHORT).show()
                    }catch (e: WindowManager.BadTokenException){
                       Log.d(TAG, e.toString())
                     }*/

                }
            }
        }

        //SocketHandler.mSocket!!.on("BeginGame", onBeginGame)
        //SocketHandler.mSocket!!.off("BeginGame")
        SocketHandler.mSocket!!.on("BeginGame"){ args->
            if(args[0] != null){
                runOnUiThread {
                    try {
                        //Almacenar la info del juego
                        //gameInfo1vs1  = ParseHelper.ParseGameInfo<GameBeginResponse>(args[0] as ResponseBody)
                        val type = object : TypeToken<GameBeginResponse>() {}.type
                        gameInfo1vs1 = parseChido<GameBeginResponse>(json = args[0].toString(), typeToken = type)


                        //Aqui acomodar los usuarios que llegan en en el response
                        characters = gameInfo1vs1.personajes

                        var id_userEmisor = user!!.get(LoginPref.KEY_USERID).toString()

                        if (id_userEmisor == gameInfo1vs1.gameInfo.player1) {
                            guessCharReceptor = gameInfo1vs1.gameInfo.character1.toInt()
                            guessCharEmisor = gameInfo1vs1.gameInfo.character2.toInt()
                        }
                        else {
                            guessCharReceptor = gameInfo1vs1.gameInfo.character2.toInt()
                            guessCharEmisor = gameInfo1vs1.gameInfo.character1.toInt()
                        }

                        idChar = guessCharReceptor

                        loadCards()
                        //Aqui iniciar
                        chrono.start()
                        loadingDialog.dismissDialog()

                        /*try{
                            loadingDialog.dismissDialog()
                        }catch (e: WindowManager.BadTokenException){
                            Log.d(TAG, e.toString())
                        }*/
                        Toast.makeText(this,"El juego ha iniciado!!!!!!", Toast.LENGTH_SHORT).show()
                    }catch (e: Exception){
                        Toast.makeText(this,"Ocurrio Un error!", Toast.LENGTH_SHORT).show()
                        SocketHandler.mSocket!!.emit("new_game",user!!.get(LoginPref.KEY_USERID))
                    }
                }
            }
        }

        //SocketHandler.mSocket!!.on("win_abandono", onAbandono)
        //SocketHandler.mSocket!!.off("win_abandono", onAbandono)
        SocketHandler.mSocket!!.on("win_abandono"){args->
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

/*
        SocketHandler.mSocket!!.on("te_vas_arrepentir"){args->
            if(args[0]!=null){
                this.runOnUiThread {
                    loadingDialog.dismissDialog()
                    finish()
                }

            }
        }
*/
        /*Faltan estos 3*/
        //Enviar respuesta de adivinacion
        //realizar emission de evento

        //cuando gana
        SocketHandler.mSocket!!.once("you_win"){args->
            customResultDialog.startLoadingDialog(true)
        }

        //cuando pierde
        SocketHandler.mSocket!!.once("you_lose"){args->
            customResultDialog.startLoadingDialog(false)
        }


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
        //SocketHandler.mSocket!!.on("message_recived", onMessageRecived)
        //SocketHandler.mSocket!!.off("message_recived", onMessageRecived)

        SocketHandler.mSocket!!.on("message_recived"){args->
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
        //SocketHandler.mSocket!!.on("respuesta_serv", onRespuestaServ)
        //SocketHandler.mSocket!!.off("respuesta_serv", onRespuestaServ)
        SocketHandler.mSocket!!.on("respuesta_serv"){ args->
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

        SocketHandler.mSocket?.emit("new_game",user!!.get(LoginPref.KEY_USERID))

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

        btn_sendMsg.setOnClickListener {
            sendMessage()
        }



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
        setContentView(R.layout.activity_board1vsai)
        generateCharacters()
        loadCards()
        btn_guess1vsia = findViewById(R.id.btn_adivina1vs1)

        btn_guess1vsia.setOnClickListener {
            GUESS = true
        }

        for (c in p) {
            if (c.personaje.id == bot) {
                adivinarChar = c
                Toast.makeText(applicationContext, "Te encontre" + adivinarChar.personaje.nombre, Toast.LENGTH_SHORT).show()
            }
        }
    }
/*
    @Override
    override fun onStart(){
        super.onStart()
        chrono.start()
    }*/

    fun loadCards() {
        recView1 = findViewById(R.id.rowP1)
        recView2 = findViewById(R.id.rowP2)
        recView3 = findViewById(R.id.rowP3)
        recView4 = findViewById(R.id.rowP4)

        clueBtn = findViewById(R.id.clue_btn)

        btnMusik = findViewById(R.id.btn_music1vs1)

        chrono = findViewById(R.id.chronos)

        Questions.getPregs(applicationContext)
        generateCharacters()
        Characters.getCharacters(characters, applicationContext, idChar)


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


        val adapter1 = CardAdapter(data1) {
            Toast.makeText(applicationContext, "aqui ando", Toast.LENGTH_SHORT).show()
            if (GUESS) {
                if (it.name == adivinarChar.personaje.nombre) {
                    Toast.makeText(applicationContext, "Ganaste", Toast.LENGTH_SHORT).show()
                    GUESS = false
                }
                else {
                    Toast.makeText(applicationContext, "Perdiste", Toast.LENGTH_SHORT).show()
                    GUESS = false
                }
            }
        }

        val adapter2 = CardAdapter(data2) {
            Toast.makeText(applicationContext, "aqui ando", Toast.LENGTH_SHORT).show()
            if (GUESS) {
                if (it.name == adivinarChar.personaje.nombre) {
                    Toast.makeText(applicationContext, "Ganaste", Toast.LENGTH_SHORT).show()
                    GUESS = false
                }
                else {
                    Toast.makeText(applicationContext, "Perdiste", Toast.LENGTH_SHORT).show()
                    GUESS = false
                }
            }
        }

        val adapter3 = CardAdapter(data3) {
            Toast.makeText(applicationContext, "aqui ando", Toast.LENGTH_SHORT).show()
            if (GUESS) {
                if (it.name == adivinarChar.personaje.nombre) {
                    Toast.makeText(applicationContext, "Ganaste", Toast.LENGTH_SHORT).show()
                    GUESS = false
                }
                else {
                    Toast.makeText(applicationContext, "Perdiste", Toast.LENGTH_SHORT).show()
                    GUESS = false
                }
            }
        }

        val adapter4 = CardAdapter(data4) {
            Toast.makeText(applicationContext, "aqui ando", Toast.LENGTH_SHORT).show()
            if (GUESS) {
                if (it.name == adivinarChar.personaje.nombre) {
                    Toast.makeText(applicationContext, "Ganaste", Toast.LENGTH_SHORT).show()
                    GUESS = false
                }
                else {
                    Toast.makeText(applicationContext, "Perdiste", Toast.LENGTH_SHORT).show()
                    GUESS = false
                }
            }
        }

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

        clueBtn.setOnClickListener {
            launchCluePop()
        }

        chrono.setOnChronometerTickListener{
            changeData()
        }
        chrono.start()
    }

    fun sendAswer(){

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
        idChar = bot
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
        val dialogAbandono = AlertDialog.Builder(this)
            .setTitle("Que perdedor")
            .setMessage("Seguro que quieres salir de la partida?")
            .setNegativeButton("Cancel") { view, _ ->
                finish()
            }
            .setPositiveButton("Accept") { view, _ ->
                when (btn_pressed) {
                    "2" -> {
                        runOnUiThread {
                            Toast.makeText(this,"Bye felicia!!", Toast.LENGTH_SHORT).show()
                        }
                        finish()
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
                        val userId = user!!.get(LoginPref.KEY_USERID).toString()
                        var id_game = "ID X"
                        var winId = "ID X"
                        if(gameInfo1vs1!=null){
                            winId = getOppositeId()
                            id_game = gameInfo1vs1.gameInfo.id
                        }
                        val d = AbandonoRequest(
                            id_game,
                            winId,
                            userId,
                            duracion
                        )
                        //Asi se envia mierda por json
                        val dataa = Json.encodeToJsonElement(d)
                        SocketHandler.mSocket!!.emit("prueba_parametros", dataa)

                        finish()
                    }
                }
            }
            .setCancelable(false)
            .create()

        dialogAbandono.show()

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

    fun launchCluePop() {
        // Inflate layout
        var inflater : LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var popupView = inflater.inflate(R.layout.popup_clue,null)

        val categSpinner: Spinner = popupView.findViewById(R.id.categ_spin)
        val pregSpinner: Spinner = popupView.findViewById(R.id.preg_spin)

        var canBut: Button = popupView.findViewById(R.id.cancel_btn)
        var aceBut: Button = popupView.findViewById(R.id.accept_btn)

        canBut.setOnClickListener {
            popupClue.dismiss()
        }

        aceBut.setOnClickListener {
            val pregunta = Questions.getPreResponse(pregSpinner.selectedItem.toString())
            val flag = Characters.checkAnswers(pregunta)
            popupClue.dismiss()
            if(flag){
                runOnUiThread{Toast.makeText(this@GameActivity, "Si", Toast.LENGTH_SHORT).show()}
            }else{
                runOnUiThread{Toast.makeText(this@GameActivity, "No", Toast.LENGTH_SHORT).show()}
            }
        }

        val categ = Questions.getCategs()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categ)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categSpinner.adapter = adapter

        categSpinner.setSelection(0)

        categSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position:Int, id: Long) {
                val preguntas = Questions.getPreText(categSpinner.selectedItem.toString())
                val preAdapter = ArrayAdapter(view!!.context, android.R.layout.simple_spinner_item, preguntas)
                pregSpinner.adapter = preAdapter
                pregSpinner.setSelection(0)
            }
        }

        pregSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position:Int, id: Long) {

            }
        }


        // Create popup 4 clues
        var w : Int = LinearLayout.LayoutParams.WRAP_CONTENT
        var h : Int = LinearLayout.LayoutParams.WRAP_CONTENT
        var focusable : Boolean = true
        popupClue = PopupWindow(popupView,w,h,focusable)

        var view : LinearLayout = findViewById(R.id.boardIa)
        popupClue.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    fun showPopupChat() {
        // Inflate layout
        var inflater : LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var popupView = inflater.inflate(R.layout.popup_chat,null)

        var recViewChat : RecyclerView = popupView.findViewById(R.id.chat)

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