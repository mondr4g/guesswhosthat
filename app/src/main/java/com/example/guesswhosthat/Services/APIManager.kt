package com.example.guesswhosthat.Services

import android.content.Context
import com.example.guesswhosthat.Helpers.GlobalVars
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class APIManager {
    //http://10.0.2.2:3000/api/
    //https://gentle-basin-19977.herokuapp.com/api/
    fun getApiObj(mContext: Context): IAPIService {
        val retro = Retrofit.Builder()
            .baseUrl(GlobalVars.URL_API_LOCAL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retro.create(IAPIService::class.java)
    }



}