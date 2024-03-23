package com.example.citysound

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TourActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour)

        val id = 1

        // Aquí puedes obtener los datos del tour de la API y mostrarlos en la interfaz de usuario
        val tourName = intent.getStringExtra("tourName") // Reemplaza con el nombre del tour obtenido de la API
        val description = intent.getStringExtra("tourDescription") // Reemplaza con la descripción del tour obtenida de la API


        val tourNameTextView = findViewById<TextView>(R.id.tourNameTextView)
        val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)

        tourNameTextView.text = tourName
        descriptionTextView.text = description

        // Habilitar los botones
        val mapsButton = findViewById<Button>(R.id.mapsButton)
        val guideButton = findViewById<Button>(R.id.guideButton)
        val pointsOfInterestButton = findViewById<Button>(R.id.pointsOfInterestButton)

        mapsButton.isEnabled = true
        guideButton.isEnabled = true
        pointsOfInterestButton.isEnabled = true
    }
}