package com.example.guesswhosthat.Models

import com.google.gson.annotations.SerializedName

data class UserLoginResponse(
    @SerializedName("message") var message: String,
    @SerializedName("_id") var id: String)