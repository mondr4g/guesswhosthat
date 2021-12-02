package com.example.guesswhosthat.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class AbandonoRequest(
    @SerializedName("gameId") var gameId: String,
    @SerializedName("winnerId") var winnerId: String,
    @SerializedName("loserId") var loserId: String,
    @SerializedName("duration") var duration: String
) : Serializable
