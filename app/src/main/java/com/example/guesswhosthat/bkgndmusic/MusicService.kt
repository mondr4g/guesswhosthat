package com.example.guesswhosthat.bkgndmusic

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.IBinder
import java.lang.IllegalArgumentException

class MusicService : Service() {
    internal lateinit var player: MediaPlayer
    override fun onBind(arg0: Intent): IBinder? {

        return null
    }
    private val musicReciver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action){
                "Pause"->{onPause()}
                "Resume"->{onResume()}
                "Menu"->{onChangeMusic("Sounds/bgmenu.wav")}
                "Battle"->{onChangeMusic("Sounds/battle.mp3")}
                "Win"->{onChangeMusic("Sounds/win.mp3")}
                "Lose"->{onChangeMusic("Sounds/lose.mp3")}
                "Stop"->{onStop()}
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val afd = applicationContext.assets.openFd("Sounds/bgmenu.wav")// as AssetFileDescriptor
        player = MediaPlayer()
        player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        player.isLooping = true // Set looping
        player.setVolume(100f, 100f)
        player.setOnPreparedListener{player.start()}
        player.prepareAsync()
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //player.start()
        val intentFilter = IntentFilter(applicationContext.packageName).apply{
            addAction("Pause")
            addAction("Resume")
            addAction("Menu")
            addAction("Battle")
            addAction("Win")
            addAction("Lose")
            addAction("Stop")
        }
        registerReceiver(musicReciver, intentFilter)
        return START_STICKY
    }

    override fun onStart(intent: Intent, startId: Int) {
        // TO DO
    }

    fun onChangeMusic(ubic: String){
        player.stop()
        player.reset()

        val afd = applicationContext.assets.openFd(ubic)
        player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        player.isLooping = true // Set looping
        player.setVolume(100f, 100f)
        player.prepareAsync()
    }

    fun onUnBind(arg0: Intent): IBinder? {
        // TO DO Auto-generated method
        return null
    }

    fun onStop() {
        onDestroy()
    }

    fun onPause() {
        player.pause()
    }

    fun onResume(){
        player.start()
    }

    override fun onDestroy() {
        player.release()
        try {
            unregisterReceiver(musicReciver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        stopSelf()
    }

    override fun onLowMemory() {

    }

    companion object {
        private val TAG: String? = null
    }

}
