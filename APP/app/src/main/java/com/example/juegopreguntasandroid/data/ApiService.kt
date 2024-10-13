package com.example.juegopreguntasandroid.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/api/preguntes")
    fun obtenerPreguntas(): Call<List<Pregunta>>

    // PUT method to update question statistics
    @PUT("/api/estadisticas/{id}")
    fun actualizarPregunta(
        @Path("id") preguntaId: Int,
        @Body stats: Estadistica // New data class for stats
    ): Call<Pregunta> // Assuming you want to return the updated Pregunta object
}
