package com.example.citysound

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class TourActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour)



        // Aquí puedes obtener los datos del tour de la API y mostrarlos en la interfaz de usuario
        val tourId = intent.getIntExtra("tourId", -1) // Obtener el ID del tour
        val tourName = intent.getStringExtra("tourName") // obtiene name api
        val description = intent.getStringExtra("tourDescription") // Obtiene desc api
        val tourImage = intent.getStringExtra("tourImage")

        val tourNameTextView = findViewById<TextView>(R.id.tourNameTextView)
        val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        val tourImageView = findViewById<ImageView>(R.id.tourImageView)

        tourNameTextView.text = tourName
        descriptionTextView.text = description

        Glide.with(this)
            .load(tourImage)
            //.placeholder(R.drawable.placeholder_image) // Opcional: Placeholder mientras se carga la imagen
            //.error(R.drawable.error_image) // Opcional: Imagen de error si falla la carga
            .into(tourImageView)

        // Habilitar los botones
        val mapsButton = findViewById<Button>(R.id.mapsButton)
        val guideButton = findViewById<Button>(R.id.guideButton)
        val pointsOfInterestButton = findViewById<Button>(R.id.pointsOfInterestButton)

        mapsButton.isEnabled = true
        guideButton.isEnabled = true
        pointsOfInterestButton.isEnabled = true

        pointsOfInterestButton.setOnClickListener {
            // Crear un Intent para abrir la actividad de la lista de puntos de interés
            val intent = Intent(this, StopsList::class.java)
            intent.putExtra("tour_id" , tourId)
            Log.d("TourActivity", "Tour ID: $tourId")
            startActivity(intent)
        }
    }
}