package com.example.juegopreguntasandroid.data

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/preguntes")
    fun getPreguntas(): Call<PreguntasResponse>
}