package com.example.guesswhosthat.Models

/*
* var emisor = data2.emisor
        var receptor = data2.receptor
        var gameId = data2.gameId
        var msj = data2.msj
* */
data class NewMessageRequest(var emisor: String, var receptor: String, var gameId: String, var msj: String)