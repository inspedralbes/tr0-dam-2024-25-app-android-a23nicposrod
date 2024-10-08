package com.example.juegopreguntasandroid.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.juegopreguntasandroid.R

class PreguntaAdapter(private val preguntas: List<Pregunta>) : RecyclerView.Adapter<PreguntaAdapter.PreguntaViewHolder>() {

    inner class PreguntaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val preguntaTextView: TextView = view.findViewById(R.id.preguntaTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreguntaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pregunta, parent, false)
        return PreguntaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PreguntaViewHolder, position: Int) {
        holder.preguntaTextView.text = preguntas[position].pregunta
    }

    override fun getItemCount(): Int {
        return preguntas.size
    }
}
