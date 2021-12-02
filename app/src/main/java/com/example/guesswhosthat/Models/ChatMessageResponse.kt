package com.example.guesswhosthat.Models

import com.google.gson.annotations.SerializedName
import java.util.*

/*
*  sender:emisor,
            reciver:receptor,
            message:msj,
            date: new Date()
* */


data class ChatMessageResponse(
    @SerializedName("sender") var emisor: String,
    @SerializedName("reciver") var receptor: String,
    @SerializedName("message") var message: String,
    @SerializedName("date") var date: String
    )
