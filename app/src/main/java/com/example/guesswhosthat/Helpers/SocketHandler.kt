package com.example.guesswhosthat.Helpers

import android.util.Log
import com.example.guesswhosthat.Helpers.GlobalVars.URL_SOCKETS_LOCAL
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {

    var mSocket: Socket?=null
    private const val TAG = "MyActivity"


    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket(URL_SOCKETS_LOCAL)
        } catch (e: URISyntaxException) {
            Log.i(TAG, "MEsto noo conecta")

        }
    }

    @Synchronized
    fun getSocket(): Socket? {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket?.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket?.disconnect()
        mSocket?.off()
        mSocket=null
    }

    @Synchronized
    fun isConnected(): Boolean {
        return if(mSocket==null){
            false
        }else{
            mSocket!!.connected()
        }
    }
}