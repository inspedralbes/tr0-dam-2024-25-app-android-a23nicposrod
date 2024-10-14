package com.example.juegopreguntasandroid

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.juegopreguntasandroid.data.Estadistica
import com.example.juegopreguntasandroid.data.Pregunta
import com.example.juegopreguntasandroid.data.RetrofitClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreguntasViewModel : ViewModel() {
    var preguntas by mutableStateOf(emptyList<Pregunta>())
    var currentPreguntaIndex by mutableStateOf(0)
    var correctAnswersCount by mutableStateOf(0)
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf("")

    private val _timeLeft = MutableStateFlow(10) // Tiempo inicial de 60 segundos
    val timeLeft: StateFlow<Int> = _timeLeft

    private var timerJob: Job? = null

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

    // Método para iniciar el temporizador
    fun startTimer() {
        timerJob?.cancel() // Cancelar cualquier temporizador existente
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000L)
                _timeLeft.value -= 1
            }
        }
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
