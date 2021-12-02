package com.example.guesswhosthat.Models

import com.google.gson.annotations.SerializedName

data class GameBeginResponse(
    @SerializedName("gameInfo") var gameInfo: GameInfoResponse,
    @SerializedName("characters") val personajes: ArrayList<Int>

    )