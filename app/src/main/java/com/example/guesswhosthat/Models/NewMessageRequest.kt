package com.example.guesswhosthat.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/*
* var emisor = data2.emisor
        var receptor = data2.receptor
        var gameId = data2.gameId
        var msj = data2.msj
* */
@kotlinx.serialization.Serializable
data class NewMessageRequest(
    @SerializedName("emisor") var emisor: String,
    @SerializedName("receptor") var receptor:String,
    @SerializedName("gameId") var gameId: String,
    @SerializedName("msj") var msj: String
) : Serializable