package com.example.citysound

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stops)

        // Obtener la parada seleccionada del Intent
        val stopActivity = intent.getParcelableExtra<Stop>("stop")

        //val tourId = intent.getIntExtra("tourId", -1)
        // Mostrar los detalles de la parada en la interfaz de usuario
        val nameTextView = findViewById<TextView>(R.id.stopNameTextView)
        val descriptionTextView = findViewById<TextView>(R.id.stopDescriptionTextView)

        nameTextView.text = stopActivity?.name
        descriptionTextView.text = stopActivity?.description
    }
}