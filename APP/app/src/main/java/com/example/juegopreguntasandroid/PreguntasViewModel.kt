package com.example.juegopreguntasandroid

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.juegopreguntasandroid.data.Estadistica
import com.example.juegopreguntasandroid.data.Pregunta
import com.example.juegopreguntasandroid.data.RetrofitClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreguntasViewModel : ViewModel() {
    var preguntas by mutableStateOf(emptyList<Pregunta>())
    var currentPreguntaIndex by mutableStateOf(0)
    var correctAnswersCount by mutableStateOf(0)
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf("")

    init {
        obtenerPreguntas()
    }

    private fun obtenerPreguntas() {
        isLoading = true
        val apiService = RetrofitClient.apiService

        apiService.obtenerPreguntas().enqueue(object : Callback<List<Pregunta>> {
            override fun onResponse(call: Call<List<Pregunta>>, response: Response<List<Pregunta>>) {
                if (response.isSuccessful) {
                    preguntas = response.body() ?: emptyList()
                    isLoading = false
                } else {
                    errorMessage = "Error en la respuesta del servidor: ${response.code()}"
                    isLoading = false
                }
            }

            override fun onFailure(call: Call<List<Pregunta>>, t: Throwable) {
                errorMessage = t.message ?: "Error desconocido"
                isLoading = false
            }
        })
    }
    fun actualizarEstadisticas(preguntaId: Int, aciertos: Int, fallos: Int) {
        val apiService = RetrofitClient.apiService
        val stats = Estadistica(aciertos, fallos)

        apiService.actualizarPregunta(preguntaId, stats).enqueue(object : Callback<Pregunta> {
            override fun onResponse(call: Call<Pregunta>, response: Response<Pregunta>) {
                if (!response.isSuccessful) {
                    errorMessage = "Error al actualizar estadísticas: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<Pregunta>, t: Throwable) {
                errorMessage = t.message ?: "Error desconocido al actualizar estadísticas"
            }
        })
    }

}
