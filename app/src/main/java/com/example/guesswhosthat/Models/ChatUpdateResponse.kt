package com.example.guesswhosthat.Models

import com.google.gson.annotations.SerializedName

data class ChatUpdateResponse(@SerializedName("message") var message: String, @SerializedName("history") var history: Array<ChatMessageResponse>)