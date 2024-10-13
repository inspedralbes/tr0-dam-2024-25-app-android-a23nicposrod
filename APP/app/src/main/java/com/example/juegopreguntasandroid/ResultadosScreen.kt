package com.example.juegopreguntasandroid

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
@Composable
fun ResultadoScreen(navController: NavHostController, correctAnswers: Int, totalPreguntas: Int) {
    val aprobado = correctAnswers > (totalPreguntas / 2) // Aprobar si tiene más de la mitad correctas
    val mensajeAprobacion = if (aprobado) "¡Felicidades! Has aprobado el cuestionario." else "Lo siento, no has aprobado el cuestionario."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Preguntas correctas: $correctAnswers de $totalPreguntas",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = mensajeAprobacion,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Navegación de vuelta a la pantalla principal (home)
        Button(onClick = { navController.navigate("home") }) {
            Text(text = "Volver")
        }
    }
}

