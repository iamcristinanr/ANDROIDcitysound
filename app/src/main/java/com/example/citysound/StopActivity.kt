package com.example.citysound

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class StopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stops)

        // Obtener la parada seleccionada del Intent
        val stopName = intent.getStringExtra("stopName")
        val stopDescription = intent.getStringExtra("stopDescription")
        //val stopActivity = intent.getParcelableExtra<Stop>("stop")
        val stopImage = intent.getStringExtra("stopImage")

        //val tourId = intent.getIntExtra("tourId", -1)
        // Mostrar los detalles de la parada en la interfaz de usuario
        val nameTextView = findViewById<TextView>(R.id.stopNameTextView)
        val descriptionTextView = findViewById<TextView>(R.id.stopDescriptionTextView)
        val stopImageView = findViewById<ImageView>(R.id.stopImageView)

        nameTextView.text = stopName
        descriptionTextView.text = stopDescription

        Glide.with(this)
            .load(stopImage)
            //.placeholder(R.drawable.placeholder_image) // Opcional: Placeholder mientras se carga la imagen
            //.error(R.drawable.error_image) // Opcional: Imagen de error si falla la carga
            .into(stopImageView)
    }
}