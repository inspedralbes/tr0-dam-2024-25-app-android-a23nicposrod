package com.example.juegopreguntasandroid.data

data class Pregunta(
    val id: Int,
    val pregunta: String,
    val respostes: List<Resposta>,
    val resposta_correcta: Int,
    val imatge: String
)
