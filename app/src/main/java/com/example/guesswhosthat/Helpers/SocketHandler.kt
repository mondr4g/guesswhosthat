package com.example.guesswhosthat.Helpers

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.guesswhosthat.Helpers.GlobalVars.URL_SOCKETS
import com.example.guesswhosthat.Helpers.GlobalVars.URL_SOCKETS_LOCAL
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URISyntaxException

object SocketHandler {

    var mSocket: Socket?=null
    private const val TAG = "MyActivity"

    @Synchronized
    fun setSocket(mContext: Context) {
        try {
            mSocket = IO.socket(URL_SOCKETS)
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