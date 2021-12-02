package com.example.guesswhosthat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WaitingRoomActivity : AppCompatActivity() {

    lateinit var recViewFriends : RecyclerView
    lateinit var recViewGlobal : RecyclerView

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_room)

        recViewFriends = findViewById(R.id.friends)
        recViewGlobal = findViewById(R.id.global)

        val dataFriends =
            Array(5) { i -> UserDetails("User $i", i, true) }

        val dataGlobal =
            Array(5) { i -> UserDetails("User " + (i+5).toString(), i+5, false) }

        val adapterFriends = FriendDetailsAdapter(dataFriends) { }
        val adapterGlobal = FriendDetailsAdapter(dataGlobal) { }

        recViewFriends.setHasFixedSize(true)
        recViewFriends.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recViewFriends.adapter = adapterFriends

        recViewGlobal.setHasFixedSize(true)
        recViewGlobal.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recViewGlobal.adapter = adapterGlobal
    }
}