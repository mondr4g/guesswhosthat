package com.example.guesswhosthat.Helpers

import com.example.guesswhosthat.Models.ErrorResponse
import com.example.guesswhosthat.Models.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody

object ParseHelper {

    fun <T> ParseGameInfo(msj: ResponseBody): T {
        val gson = Gson()
        val type = object : TypeToken<T>() {}.type
        return gson.fromJson<T?>(msj!!.charStream(), type)
    }
}