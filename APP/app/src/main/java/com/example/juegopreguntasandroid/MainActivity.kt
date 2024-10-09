package com.example.juegopreguntasandroid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.juegopreguntasandroid.ui.theme.JuegoPreguntasAndroidTheme
import com.example.juegopreguntasandroid.data.Pregunta
import com.example.juegopreguntasandroid.data.PreguntasResponse
import okhttp3.*
import com.google.gson.Gson
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa el layout principal usando Compose
        setContent {
            JuegoPreguntasAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShowXmlLayout(this)
                }
            }
        }
    }

    @Composable
    fun ShowXmlLayout(activity: ComponentActivity) {
        // Usa AndroidView para inflar el layout XML
        AndroidView(
            factory = { context ->
                // Infla el layout XML principal
                val view = LayoutInflater.from(context).inflate(R.layout.activity_main, null)

                // Encuentra el botón con id `but1`
                val button = view.findViewById<Button>(R.id.but1)

                // Configura un listener para cambiar de layout cuando se presiona el botón
                button.setOnClickListener {
                    // Cambia al segundo layout cuando el botón es presionado
                    activity.setContentView(R.layout.item_pregunta)

                    // Lógica para cargar la pregunta y las respuestas en el segundo layout
                    obtenerPreguntas { pregunta ->
                        if (pregunta != null) {
                            // Encuentra las vistas del segundo layout
                            val preguntaTextView =
                                activity.findViewById<TextView>(R.id.preguntaTextView)
                            val respuestaButton1 = activity.findViewById<Button>(R.id.respuestaButton1)
                            val respuestaButton2 = activity.findViewById<Button>(R.id.respuestaButton2)
                            val respuestaButton3 = activity.findViewById<Button>(R.id.respuestaButton3)
                            val respuestaButton4 = activity.findViewById<Button>(R.id.respuestaButton4)

                            // Actualiza la pregunta y las respuestas
                            preguntaTextView.text = pregunta.pregunta
                            respuestaButton1.text = pregunta.respostes[0].etiqueta
                            respuestaButton2.text = pregunta.respostes[1].etiqueta
                            respuestaButton3.text = pregunta.respostes[2].etiqueta
                            respuestaButton4.text = pregunta.respostes[3].etiqueta

                            // Configura los listeners para manejar la selección de respuestas
                            respuestaButton1.setOnClickListener {
                                comprobarRespuesta(
                                    pregunta.respostes[0].id,
                                    pregunta.resposta_correcta,
                                    activity
                                )
                            }

                            respuestaButton2.setOnClickListener {
                                comprobarRespuesta(
                                    pregunta.respostes[1].id,
                                    pregunta.resposta_correcta,
                                    activity
                                )
                            }

                            respuestaButton3.setOnClickListener {
                                comprobarRespuesta(
                                    pregunta.respostes[2].id,
                                    pregunta.resposta_correcta,
                                    activity
                                )
                            }

                            respuestaButton4.setOnClickListener {
                                comprobarRespuesta(
                                    pregunta.respostes[3].id,
                                    pregunta.resposta_correcta,
                                    activity
                                )
                            }
                        } else {
                            // Manejar el caso en que no se encontró la pregunta
                            Log.e("MainActivity", "No se encontró la pregunta.")
                        }
                    }
                }
                view // Asegúrate de devolver la vista inflada
            }
        )
    }

    private fun comprobarRespuesta(respuestaSeleccionadaId: Int, respuestaCorrectaId: Int, activity: ComponentActivity) {
        // Compara el ID de la respuesta seleccionada con el ID de la respuesta correcta
        if (respuestaSeleccionadaId == respuestaCorrectaId) {
            // Respuesta correcta
            activity.setContentView(R.layout.layout_respuesta_correcta) // Un layout para mostrar que es correcta

            // Aquí puedes añadir más lógica, como un mensaje de éxito
            val messageTextView = activity.findViewById<TextView>(R.id.textoRespuestaCorrecta)
            messageTextView.text = "¡Correcto! La respuesta es correcta."
        } else {
            // Respuesta incorrecta
            activity.setContentView(R.layout.layout_respuesta_incorrecta) // Un layout para mostrar que es incorrecta

            // Aquí puedes añadir más lógica, como un mensaje de error
            val messageTextView = activity.findViewById<TextView>(R.id.textoRespuestaIncorrecta)
            messageTextView.text = "Incorrecto. Intenta de nuevo."
        }
    }

    private fun obtenerPreguntas(callback: (Pregunta?) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://198.168.0.155:3000/api/preguntas") // Cambia esto a la URL de tu servidor
            .build()

        Log.d("MainActivity", "Realizando solicitud para obtener preguntas...")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "Error al obtener preguntas: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.e("MainActivity", "Error en la respuesta: ${response.code}")
                        throw IOException("Unexpected code $response")
                    }

                    val json = response.body?.string() ?: return
                    Log.d("MainActivity", "Respuesta recibida: $json")

                    val preguntasResponse = Gson().fromJson(json, PreguntasResponse::class.java)

                    // Obtén la pregunta con id 1
                    val preguntaConId1 = preguntasResponse.preguntes.find { it.id == 1 }
                    Log.d("MainActivity", "Pregunta obtenida: ${preguntaConId1?.pregunta ?: "No se encontró la pregunta."}")
                    callback(preguntaConId1)
                }
            }
        })
    }
}
