package com.example.guesswhosthat.Helpers

import com.example.guesswhosthat.Models.ErrorResponse
import com.example.guesswhosthat.Models.Response
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONArray
import java.lang.reflect.Type

object ParseHelper {

    fun <T> ParseGameInfo(msj: ResponseBody): T {
        val gson = Gson()
        val type = object : TypeToken<T>() {}.type
        return gson.fromJson<T?>(msj!!.charStream(), type)
    }

    fun <T> ParseJsonObject(msj: String): T {
        val gson = Gson()
        val type = object : TypeToken<T>() {}.type
        return gson.fromJson<T>(msj, type)
    }

    inline fun <reified T> parseChido(json: String, typeToken: Type): T {
        val gson = GsonBuilder().create()
        return gson.fromJson<T>(json, typeToken)
    }
}