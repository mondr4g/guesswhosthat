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
    @SerializedName("player1") var p1: String,
    @SerializedName("player2") var p2: String,
    @SerializedName("winner") var win: String,
    @SerializedName("loser") var ls: String,
    @SerializedName("character1") var c1: String,
    @SerializedName("character2") var c2: String,
    @SerializedName("duration") var tiempo: String,
    @SerializedName("finished") var fin: Boolean,
    @SerializedName("chat") var chat: Array<ChatMessageResponse>
    )