package com.example.guesswhosthat.Helpers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.IOException

data class Pregs(
    @SerializedName("texto") var texto: String,
    @SerializedName("categoria") var categoria: String,
    @SerializedName("valor") var valor: String,
)

object Questions {
    private var preguntas: MutableList<Pregs> = ArrayList()

    fun getPregs(ctx: Context){
        val jsonFileString = getJsonDataFromAsset(ctx)
        val gson = Gson()
        val listType = object: TypeToken<List<Pregs>>() {}.type
        if(jsonFileString != null){
            preguntas = gson.fromJson(jsonFileString,listType)
        }else{
            //Condicion para que saque a ambos de la partida; fallo la lectura del json
        }
    }

    fun getJsonDataFromAsset(context:Context): String?{
        val jsonString: String
        try {
            jsonString = context.assets.open("CharJson/preg.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}