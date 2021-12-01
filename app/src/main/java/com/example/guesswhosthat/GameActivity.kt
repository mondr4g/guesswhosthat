package com.example.guesswhosthat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.graphics.Typeface
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guesswhosthat.Helpers.Characters
import com.example.guesswhosthat.Helpers.PsjObj


class GameActivity : AppCompatActivity() {

    private lateinit var recView1 : RecyclerView
    private lateinit var recView2 : RecyclerView
    private lateinit var recView3 : RecyclerView
    private lateinit var recView4 : RecyclerView

    private lateinit var characters : Array<Int>
    private var bot : Int = 0

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board1vs1)

        recView1 = findViewById(R.id.rowP1)
        recView2 = findViewById(R.id.rowP2)
        recView3 = findViewById(R.id.rowP3)
        recView4 = findViewById(R.id.rowP4)

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