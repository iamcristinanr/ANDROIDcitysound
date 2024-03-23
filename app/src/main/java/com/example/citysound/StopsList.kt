package com.example.citysound

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StopsList : AppCompatActivity(), StopListAdapter.OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerviewstops)

        // Configurar RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewStops)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crear y establecer el adaptador para el RecyclerView
        val stopListAdapter = StopListAdapter(getSampleStops(), this)
        recyclerView.adapter = stopListAdapter
    }

    // Implementar el método onItemClick del listener del adaptador
    override fun onItemClick(stop: Stop) {
        // Abrir la actividad de detalles de la parada
        val intent = Intent(this, StopActivity::class.java)
        intent.putExtra("stop", stop)
        startActivity(intent)
    }

    // Esta es una función de ejemplo que devuelve una lista de paradas de ejemplo
    private fun getSampleStops(): List<Stop> {
        val stops = mutableListOf<Stop>()
        // Agrega aquí las paradas que desees mostrar en la lista
        stops.add(Stop("Stop 1", "Description 1"))
        stops.add(Stop("Stop 2", "Description 2"))
        stops.add(Stop("Stop 3", "Description 3"))
        return stops
    }
}
