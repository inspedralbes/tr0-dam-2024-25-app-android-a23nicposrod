package com.example.juegopreguntasandroid

import android.util.Log // Importar Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.juegopreguntasandroid.data.Pregunta
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.*

@Composable
fun PreguntasScreen(navController: NavHostController) {
    val viewModel: PreguntasViewModel = remember { PreguntasViewModel() }

    val preguntas = viewModel.preguntas
    val currentPreguntaIndex = viewModel.currentPreguntaIndex
    val correctAnswersCount = viewModel.correctAnswersCount
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val timeLeft by viewModel.timeLeft.collectAsState() // Estado para el temporizador

    // Lanzar el temporizador cuando la pantalla se carga
    LaunchedEffect(Unit) {
        viewModel.startTimer()
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    } else if (preguntas.isNotEmpty() && currentPreguntaIndex < preguntas.size) {
        val preguntaActual = preguntas[currentPreguntaIndex]
        ShowPregunta(preguntaActual, navController, viewModel)

        // Mostrar el temporizador
        Text(text = "Tiempo restante: $timeLeft segundos", modifier = Modifier.padding(16.dp))
    } else if (preguntas.isEmpty()) {
        Text(text = errorMessage, modifier = Modifier.padding(16.dp))
    }

    // Redirigir a la pantalla de resultados si el tiempo se acaba
    if (timeLeft <= 0) {
        LaunchedEffect(Unit) {
            navController.navigate("resultado/$correctAnswersCount/${currentPreguntaIndex}")
        }
    }

    // Redirigir a resultados si se contestaron todas las preguntas
    if (currentPreguntaIndex >= preguntas.size && preguntas.isNotEmpty()) {
        LaunchedEffect(currentPreguntaIndex) {
            navController.navigate("resultado/$correctAnswersCount/${preguntas.size}")
        }
    }
}

@Composable
fun ShowPregunta(pregunta: Pregunta, navController: NavHostController, viewModel: PreguntasViewModel) {
    val context = LocalContext.current

    AndroidView(factory = {
        LayoutInflater.from(context).inflate(R.layout.item_pregunta, null)
    }, update = { view ->
        val preguntaTextView: TextView = view.findViewById(R.id.preguntaTextView)
        val imageView: ImageView = view.findViewById(R.id.imagenJuego)
        val respuestaButton1: Button = view.findViewById(R.id.respuestaButton1)
        val respuestaButton2: Button = view.findViewById(R.id.respuestaButton2)
        val respuestaButton3: Button = view.findViewById(R.id.respuestaButton3)
        val respuestaButton4: Button = view.findViewById(R.id.respuestaButton4)

        preguntaTextView.text = pregunta.pregunta

        pregunta.imatge?.let { imatge ->
            val imageLoader = context.imageLoader
            val request = ImageRequest.Builder(context)
                .data(imatge)
                .target(imageView)
                .build()

            imageLoader.enqueue(request)
        }

        respuestaButton1.text = pregunta.respostes[0].etiqueta
        respuestaButton2.text = pregunta.respostes[1].etiqueta
        respuestaButton3.text = pregunta.respostes[2].etiqueta
        respuestaButton4.text = pregunta.respostes[3].etiqueta

        val preguntaId = pregunta.id

        respuestaButton1.setOnClickListener { comprobarRespuesta(pregunta.respostes[0].id, pregunta.resposta_correcta, preguntaId, navController, viewModel) }
        respuestaButton2.setOnClickListener { comprobarRespuesta(pregunta.respostes[1].id, pregunta.resposta_correcta, preguntaId, navController, viewModel) }
        respuestaButton3.setOnClickListener { comprobarRespuesta(pregunta.respostes[2].id, pregunta.resposta_correcta, preguntaId, navController, viewModel) }
        respuestaButton4.setOnClickListener { comprobarRespuesta(pregunta.respostes[3].id, pregunta.resposta_correcta, preguntaId, navController, viewModel) }
    })
}

private fun comprobarRespuesta(respuestaSeleccionadaId: Int, respuestaCorrectaId: Int, preguntaId: Int, navController: NavHostController, viewModel: PreguntasViewModel) {
    if (respuestaSeleccionadaId == respuestaCorrectaId) {
        viewModel.correctAnswersCount++
        viewModel.actualizarEstadisticas(preguntaId, 1, 0)
    } else {
        viewModel.actualizarEstadisticas(preguntaId, 0, 1)
    }

    if (viewModel.currentPreguntaIndex < viewModel.preguntas.size - 1) {
        viewModel.currentPreguntaIndex += 1
    } else {
        navController.navigate("resultado/${viewModel.correctAnswersCount}/${viewModel.preguntas.size}")
    }
}
