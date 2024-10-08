package com.example.juegopreguntasandroid

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.juegopreguntasandroid.R
import com.example.juegopreguntasandroid.data.ApiService
import com.example.juegopreguntasandroid.data.PreguntasResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:3000") // Reemplaza con tu URL base
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Llamar a la API
        fetchPreguntas()
    }

    private fun fetchPreguntas() {
        apiService.getPreguntas().enqueue(object : Callback<PreguntasResponse> {
            override fun onResponse(call: Call<PreguntasResponse>, response: Response<PreguntasResponse>) {
                if (response.isSuccessful) {
                    val preguntas = response.body()?.preguntes
                    // Aquí puedes actualizar tu UI con las preguntas
                    preguntas?.let {
                        // Por ejemplo, puedes mostrar la primera pregunta en un Toast
                        Toast.makeText(this@MainActivity, it[0].pregunta, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error en la respuesta", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PreguntasResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error en la llamada a la API", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
