package com.example.guesswhosthat.Helpers

import com.example.guesswhosthat.Models.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody

object ErrorResponseHelper {
    fun getErrorMessage(errorBody: ResponseBody?) : String? {
        val gson = Gson()
        val type = object : TypeToken<ErrorResponse>() {}.type
        return gson.fromJson<ErrorResponse?>(errorBody!!.charStream(), type)?.message
    }
}