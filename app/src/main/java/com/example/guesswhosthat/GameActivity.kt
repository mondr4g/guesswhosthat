package com.example.guesswhosthat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.graphics.Typeface
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class GameActivity : AppCompatActivity() {

    private lateinit var recView1 : RecyclerView
    private lateinit var recView2 : RecyclerView
    private lateinit var recView3 : RecyclerView
    private lateinit var recView4 : RecyclerView


    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board1vs1)

        recView1 = findViewById(R.id.rowP1)
        recView2 = findViewById(R.id.rowP2)
        recView3 = findViewById(R.id.rowP3)
        recView4 = findViewById(R.id.rowP4)

        val data =
            Array(8) { i -> Character("Personaje $i","path") }

        val adapter = CardAdapter(data) {

        }

        recView1.setHasFixedSize(true)
        recView1.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recView1.adapter = adapter

        recView2.setHasFixedSize(true)
        recView2.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recView2.adapter = adapter

        recView3.setHasFixedSize(true)
        recView3.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recView3.adapter = adapter

        recView4.setHasFixedSize(true)
        recView4.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recView4.adapter = adapter
    }
}