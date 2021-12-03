package com.example.guesswhosthat.Helpers

import android.content.Context
import android.util.Log
import android.widget.Toast
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
    private lateinit var pchar: PsjObj

    fun getCharacters(charIds:Array<Int>, ctx: Context, id: Int){
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
                        if(id == psj.id)pchar = helper
                        break
                    }
                }
            }
            personajes.shuffle()
        }else{
            //Condicion para que saque a ambos de la partida; fallo la lectura del json
        }
    }

    fun getPlayerChar(): PsjObj {
        return pchar
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

    fun getCharacterInfo(){

    }

    fun checkAnswers(pregunta: Pregs): Boolean{
        var flag = false
        when(pregunta.categoria){
            "tez"->{ flag = pchar.personaje.tez == pregunta.valor }
            "accesorios"->{ flag = pchar.personaje.accesorios == pregunta.valor }
            "genero"->{ flag = pchar.personaje.genero == pregunta.valor }
            "color de cabello"->{ flag = pchar.personaje.cabello.color == pregunta.valor }
            "tamaño de cabello"->{ flag = pchar.personaje.cabello.tamaño == pregunta.valor }
            "tipo de cabello"->{ flag = pchar.personaje.cabello.tipo == pregunta.valor }
            "orejas"->{ flag = pchar.personaje.rostro.orejas == pregunta.valor }
            "nariz"->{ flag = pchar.personaje.rostro.nariz == pregunta.valor }
            "labios"->{ flag = pchar.personaje.rostro.labios == pregunta.valor }
            "color de ojos"->{ flag = pchar.personaje.rostro.ojos.color == pregunta.valor }
            "tipo de ojos"->{ flag = pchar.personaje.rostro.ojos.tipo == pregunta.valor }
            "cejas"->{ flag = pchar.personaje.rostro.cejas == pregunta.valor }
            "tipo de rostro"->{ flag = pchar.personaje.rostro.tipo == pregunta.valor }
        }
        return flag
    }

}