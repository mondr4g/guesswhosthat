package com.example.guesswhosthat.Services

import com.example.guesswhosthat.Models.UserLoginRequest
import com.example.guesswhosthat.Models.UserLoginResponse
import com.example.guesswhosthat.Models.newUserRequest
import retrofit2.Response
import retrofit2.http.*

interface IAPIService {

    @POST("users/login")
    @Headers("Accept: application/json")
    suspend fun userLogin(@Body user: UserLoginRequest): Response<UserLoginResponse>

    @POST("users/create")
    @Headers("Accept: application/json")
    suspend fun newUser(@Body user: newUserRequest): Response<UserLoginResponse>



    @POST
    @Multipart
    @Headers("Accept: application/json")

    suspend fun <T,E> postToApi(@Url url: String, @Body objectPram: @JvmSuppressWildcards T): Response<E>

}