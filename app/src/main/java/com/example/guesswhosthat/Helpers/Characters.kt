package com.example.guesswhosthat.Helpers

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.IOException

data class Cabe(
    @SerializedName("color") val color: String,
    @SerializedName("tamaño") val tamaño: String,
    @SerializedName("tipo") val tipo: String
)

data class Ojo(
    @SerializedName("color") val color: String,
    @SerializedName("tipo") val tipo: String
)

data class Rost(
    @SerializedName("orejas") val orejas: String,
    @SerializedName("nariz") val nariz: String,
    @SerializedName("labios") val labios: String,
    @SerializedName("ojos") val ojos: Ojo,
    @SerializedName("cejas") val cejas: String,
    @SerializedName("tipo") val tipo: String
)

data class Pobj(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("tez") val tez: String,
    @SerializedName("accesorios") val accesorios: String,
    @SerializedName("genero") val genero: String,
    @SerializedName("cabello") val cabello: Cabe,
    @SerializedName("rostro") val rostro: Rost
)

object Characters {
    private lateinit var personajes: MutableList<Pobj>

    fun getCharacters(charIds:Array<Int>, ctx: Context){
        val jsonFileString = getJsonDataFromAsset(ctx)
        val gson = Gson()
        val listType = object: TypeToken<List<Pobj>>() {}.type
        if(jsonFileString != null){
            var Temp:List<Pobj> = gson.fromJson(jsonFileString,listType)
            for(psjId in charIds) {
                Temp.forEach{psj ->
                    run {
                        if (psjId == psj.id) {
                            personajes.add(psj)
                        }
                    }
                }
            }
        }else{
            //Condicion para que saque a ambos de la partida; fallo la lectura del json
        }
    }

    fun emptyCharsList(){
        personajes.clear();
    }

    fun getJsonDataFromAsset(context:Context): String?{
        val jsonString: String
        try {
            jsonString = context.assets.open("CharJson/personajes.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}