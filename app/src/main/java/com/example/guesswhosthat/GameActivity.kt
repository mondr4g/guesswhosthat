package com.example.guesswhosthat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.graphics.Typeface
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guesswhosthat.Helpers.Characters
import com.example.guesswhosthat.Helpers.PsjObj
import android.widget.Chronometer.OnChronometerTickListener





class GameActivity : AppCompatActivity() {

    private lateinit var recView1 : RecyclerView
    private lateinit var recView2 : RecyclerView
    private lateinit var recView3 : RecyclerView
    private lateinit var recView4 : RecyclerView

    private lateinit var chrono: Chronometer

    private lateinit var characters : Array<Int>
    private var bot : Int = 0

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btn_pressed = intent.getStringExtra(BTN_PRESSED)

        when (btn_pressed) {
            "0" -> prepare1vs1()
            "1" -> prepare1vsFriends()
            "2" -> prepare1vsAI()
        }
    }

    fun prepare1vs1() {
        setContentView(R.layout.activity_board1vs1)

        var loadingDialog : LoadingDialog = LoadingDialog(this)
        loadingDialog.startLoadingDialog()
    }

    fun prepare1vsFriends() {
        setContentView(R.layout.activity_board1vs1)
    }

    fun prepare1vsAI() {
        setContentView(R.layout.activity_board1vsai)

        recView1 = findViewById(R.id.rowP1)
        recView2 = findViewById(R.id.rowP2)
        recView3 = findViewById(R.id.rowP3)
        recView4 = findViewById(R.id.rowP4)

        chrono = findViewById(R.id.chronos)

        generateCharacters()
        Characters.getCharacters(characters, applicationContext)


        val p : MutableList<PsjObj> = Characters.getPersonajes()

        Toast.makeText(applicationContext, p[0].personaje.nombre, Toast.LENGTH_SHORT).show()

        val data1 =
            Array(6) { i -> Character(p[i].personaje.nombre,p[i].resourceId) }

        val data2 =
            Array(6) { i -> Character(p[i+6].personaje.nombre,p[i+6].resourceId) }

        val data3 =
            Array(6) { i -> Character(p[i+12].personaje.nombre,p[i+12].resourceId) }

        val data4 =
            Array(6) { i -> Character(p[i+18].personaje.nombre,p[i+18].resourceId) }


        val adapter1 = CardAdapter(data1) {

        }

        val adapter2 = CardAdapter(data2) {

        }

        val adapter3 = CardAdapter(data3) {

        }

        val adapter4 = CardAdapter(data4) {

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

        chrono.setOnChronometerTickListener{
            val time = SystemClock.elapsedRealtime() - chrono.base
            val h = (time / 3600000).toInt()
            val m = (time - h * 3600000).toInt() / 60000
            val s = (time - h * 3600000 - m * 60000).toInt() / 1000
            val hh = if (h < 10) "0$h" else h.toString() + ""
            val mm = if (m < 10) "0$m" else m.toString() + ""
            val ss = if (s < 10) "0$s" else s.toString() + ""
            chrono.text = "$hh:$mm:$ss"
        }
    }

    @Override
    override fun onStart(){
        super.onStart()
        chrono.start()
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
}