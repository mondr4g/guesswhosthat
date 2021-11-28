package com.example.guesswhosthat.Models

data class Response<out T> (val IsSuccess:Boolean, val Message: String, val Result: T)