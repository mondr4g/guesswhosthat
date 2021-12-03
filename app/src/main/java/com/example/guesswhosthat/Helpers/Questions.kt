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
    var preguntas: MutableList<Pregs> = ArrayList()

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

    fun getCategs(): List<String> {
        var temp: MutableList<String> = ArrayList()
        for (pregunta in preguntas){
            if(!temp.contains(pregunta.categoria))
                temp.add(pregunta.categoria)
        }
        return temp
    }

    fun getPreText(categoria: String): List<String>{
        var temp: MutableList<String> = ArrayList()
        for (pregunta in preguntas){
            if(pregunta.categoria == categoria)
                temp.add(pregunta.texto)
        }
        return temp
    }

    fun getPreResponse(texto: String): Pregs{
        lateinit var temp: Pregs
        for (pregunta in preguntas){
            if(pregunta.texto == texto) {
                temp = pregunta
                break
            }
        }
        return temp
    }
}