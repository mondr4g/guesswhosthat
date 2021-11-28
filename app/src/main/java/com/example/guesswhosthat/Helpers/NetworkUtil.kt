package com.example.guesswhosthat.Helpers

import android.content.Context
import android.net.NetworkInfo

import android.net.ConnectivityManager



object NetworkUtil {
    final fun isOnline(mContext: Context):Boolean{
        val connectionManager: ConnectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectionManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        return isConnected
    }
}