package com.example.guesswhosthat.Helpers

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.IOException

data class Cabe(
    @SerializedName("color") var color: String,
    @SerializedName("tamaño") var tamaño: String,
    @SerializedName("tipo") var tipo: String
)

data class Ojo(
    @SerializedName("color") var color: String,
    @SerializedName("tipo") var tipo: String
)

data class Rost(
    @SerializedName("orejas") var orejas: String,
    @SerializedName("nariz") var nariz: String,
    @SerializedName("labios") var labios: String,
    @SerializedName("ojos") var ojos: Ojo,
    @SerializedName("cejas") var cejas: String,
    @SerializedName("tipo") var tipo: String
)

data class Pobj(
    @SerializedName("id") var id: Int,
    @SerializedName("nombre") var nombre: String,
    @SerializedName("tez") var tez: String,
    @SerializedName("accesorios") var accesorios: String,
    @SerializedName("genero") var genero: String,
    @SerializedName("cabello") var cabello: Cabe,
    @SerializedName("rostro") var rostro: Rost
)

data class PsjObj(
    @SerializedName("resourceId") var resourceId: Int,
    @SerializedName("personaje") var personaje: Pobj
)

object Characters {
    private var personajes: MutableList<PsjObj> = ArrayList()

    fun getCharacters(charIds:Array<Int>, ctx: Context){
        val jsonFileString = getJsonDataFromAsset(ctx)
        var rid: Int
        var helper: PsjObj
        val gson = Gson()
        val listType = object: TypeToken<List<Pobj>>() {}.type
        if(jsonFileString != null){
            var Temp:List<Pobj> = gson.fromJson(jsonFileString,listType)
            for(psjId in charIds) {
                for(psj in Temp){
                    if (psjId == psj.id) {
                        rid = ctx.resources.getIdentifier("i${psj.id}",
                            "drawable", ctx.packageName)
                        helper = PsjObj(rid, psj)
                        personajes.add(helper)
                        break
                    }
                }
            }
        }else{
            //Condicion para que saque a ambos de la partida; fallo la lectura del json
        }
    }

    fun emptyCharsList(){
        personajes.clear()
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

    fun getPersonajes() : MutableList<PsjObj> {
        return personajes
    }

}