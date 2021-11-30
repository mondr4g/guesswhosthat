package com.example.guesswhosthat.bkgndmusic

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {
    internal lateinit var player: MediaPlayer
    override fun onBind(arg0: Intent): IBinder? {

        return null
    }

    override fun onCreate() {
        super.onCreate()
        val afd = applicationContext.assets.openFd("Sounds/bgmenu.wav")// as AssetFileDescriptor
        player = MediaPlayer()
        player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        //player.setDataSource(afd.fileDescriptor)
        player.isLooping = true // Set looping
        player.setVolume(100f, 100f)
        player.setOnPreparedListener{player.start()}
        player.prepareAsync()
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //player.start()
        return START_STICKY
    }

    override fun onStart(intent: Intent, startId: Int) {
        // TO DO
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
        player.stop()
        player.release()
    }

    override fun onLowMemory() {

    }

    companion object {
        private val TAG: String? = null
    }
}
