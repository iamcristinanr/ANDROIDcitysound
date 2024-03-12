package com.example.citysound

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SearchTour : AppCompatActivity() {
    private lateinit var cityEditText: EditText
    private lateinit var tourNameEditText: EditText
    private lateinit var guideNameEditText: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchtour)

        cityEditText = findViewById(R.id.editTextCity)
        tourNameEditText = findViewById(R.id.editTextTourName)
        guideNameEditText = findViewById(R.id.editTextGuideName)
        searchButton = findViewById(R.id.buttonSearch)

        searchButton.setOnClickListener {
            val city = cityEditText.text.toString()
            val tourName = tourNameEditText.text.toString()
            val guideName = guideNameEditText.text.toString()

            // Aquí debes realizar la búsqueda en la API utilizando los valores ingresados por el usuario

            // Una vez que hayas obtenido los resultados de la búsqueda, puedes iniciar la actividad del tour
            val intent = Intent(this, TourActivity::class.java)
            // Aquí puedes pasar datos adicionales a la actividad del tour si es necesario
            startActivity(intent)
        }
    }
}