package com.example.juegopreguntasandroid.data

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("ruta/a/tu/json") // Cambia esto por la ruta real a tu JSON en el servidor
    fun obtenerPreguntas(): Call<Cuestionario>
}