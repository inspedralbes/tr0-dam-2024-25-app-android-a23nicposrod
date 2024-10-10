package com.example.juegopreguntasandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.juegopreguntasandroid.ui.theme.JuegoPreguntasAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JuegoPreguntasAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(navController)
            }
            composable("preguntas") {
                PreguntasScreen(navController)
            }
            composable("resultado/{correctAnswers}/{totalQuestions}") { backStackEntry ->
                val correctAnswers = backStackEntry.arguments?.getString("correctAnswers")?.toInt() ?: 0
                val totalQuestions = backStackEntry.arguments?.getString("totalQuestions")?.toInt() ?: 0
                ResultadoScreen(navController, correctAnswers, totalQuestions)
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenido al Juego de Preguntas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 50.dp)
        )
        Button(
            onClick = { navController.navigate("preguntas") },
            modifier = Modifier
                .width(300.dp) // Ajustar el ancho
                .padding(bottom = 16.dp) // Espacio entre botones
        ) {
            Text(text = "Jugar")
        }
        Button(
            onClick = { /* Acción para estadísticas */ },
            modifier = Modifier.width(300.dp) // Ajustar el ancho
        ) {
            Text(text = "Estadísticas")
        }
    }
}
