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
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import com.squareup.picasso.Picasso

@Composable
fun PreguntasScreen(navController: NavHostController) {
    // Usar ViewModel
    val viewModel: PreguntasViewModel = remember { PreguntasViewModel() }

    // Acceder a las variables desde el ViewModel
    val preguntas = viewModel.preguntas
    val currentPreguntaIndex = viewModel.currentPreguntaIndex
    val correctAnswersCount = viewModel.correctAnswersCount
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    } else if (preguntas.isNotEmpty() && currentPreguntaIndex < preguntas.size) {
        val preguntaActual = preguntas[currentPreguntaIndex]
        ShowPregunta(preguntaActual, navController, viewModel)
    } else if (preguntas.isEmpty()) {
        Text(text = errorMessage, modifier = Modifier.padding(16.dp))
    }

    // Navegar a la pantalla de resultados solo si se han respondido todas las preguntas
    if (currentPreguntaIndex >= preguntas.size && preguntas.isNotEmpty()) {
        LaunchedEffect(currentPreguntaIndex) {
            navController.navigate("resultado/$correctAnswersCount/${preguntas.size}")
        }
    }
}

@Composable
fun ShowPregunta(pregunta: Pregunta, navController: NavHostController, viewModel: PreguntasViewModel) {
    AndroidView(factory = { context ->
        LayoutInflater.from(context).inflate(R.layout.item_pregunta, null)
    }, update = { view ->
        val preguntaTextView: TextView = view.findViewById(R.id.preguntaTextView)
        val imageView: ImageView = view.findViewById(R.id.imagenJuego)
        val respuestaButton1: Button = view.findViewById(R.id.respuestaButton1)
        val respuestaButton2: Button = view.findViewById(R.id.respuestaButton2)
        val respuestaButton3: Button = view.findViewById(R.id.respuestaButton3)
        val respuestaButton4: Button = view.findViewById(R.id.respuestaButton4)

        // Configurar los componentes del layout
        preguntaTextView.text = pregunta.pregunta

        // Cargar la imagen usando Picasso
        pregunta.imagenUrl?.let { imageUrl ->
            Log.d("PreguntasScreen", "Cargando imagen desde URL: $imageUrl") // Log de la URL
            // Usar Picasso para cargar la imagen
            Picasso.get()
                .load(imageUrl) // Cargar desde la URL
                .into(imageView) // Establecer la imagen en el ImageView
        } ?: run {
            imageView.setImageResource(0) // Limpia la imagen si no hay URL
        }

        // Establecer las respuestas
        respuestaButton1.text = pregunta.respostes[0].etiqueta
        respuestaButton2.text = pregunta.respostes[1].etiqueta
        respuestaButton3.text = pregunta.respostes[2].etiqueta
        respuestaButton4.text = pregunta.respostes[3].etiqueta

        // Configurar los listeners para los botones
        respuestaButton1.setOnClickListener { comprobarRespuesta(pregunta.respostes[0].id, pregunta.resposta_correcta, navController, viewModel) }
        respuestaButton2.setOnClickListener { comprobarRespuesta(pregunta.respostes[1].id, pregunta.resposta_correcta, navController, viewModel) }
        respuestaButton3.setOnClickListener { comprobarRespuesta(pregunta.respostes[2].id, pregunta.resposta_correcta, navController, viewModel) }
        respuestaButton4.setOnClickListener { comprobarRespuesta(pregunta.respostes[3].id, pregunta.resposta_correcta, navController, viewModel) }
    })
}

// Comprobar respuesta ahora usa el ViewModel
private fun comprobarRespuesta(respuestaSeleccionadaId: Int, respuestaCorrectaId: Int, navController: NavHostController, viewModel: PreguntasViewModel) {
    if (respuestaSeleccionadaId == respuestaCorrectaId) {
        viewModel.correctAnswersCount++ // Incrementar si la respuesta es correcta
    }

    // Cambiar de pregunta
    if (viewModel.currentPreguntaIndex < viewModel.preguntas.size - 1) {
        viewModel.currentPreguntaIndex += 1
    } else {
        navController.navigate("resultado/${viewModel.correctAnswersCount}/${viewModel.preguntas.size}") // Navegar a resultados
    }
}
