package com.example.guesswhosthat.Models

import com.google.gson.annotations.SerializedName

/*
* roomId: {type:String},
    player1: {
        type: Schema.Types.ObjectId,
        ref: "User",
        required:true},
    player2: {
        type: Schema.Types.ObjectId,
        ref: "User",
        required:true},
    winner: {
        type: Schema.Types.ObjectId,
        ref: "User", },
    loser: {
        type: Schema.Types.ObjectId,
        ref: "User", },
    character1: {type: Number, required:true},
    character2: {type: Number, required:true},
    duration: {type:String},
    finished: Boolean
* */

data class GameInfoResponse(
    @SerializedName("_id") var id: String,
    @SerializedName("player1") var player1: String,
    @SerializedName("player2") var player2: String,
    @SerializedName("winner") var winner: String,
    @SerializedName("loser") var loser: String,
    @SerializedName("character1") var character1: String,
    @SerializedName("character2") var character2: String,
    @SerializedName("duration") var duration: String,
    @SerializedName("finished") var finished: Boolean,
    @SerializedName("chat") var chat: Array<ChatMessageResponse>
    )