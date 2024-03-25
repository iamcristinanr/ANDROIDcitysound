package com.example.citysound

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView

class TourActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour)


        lateinit var bottomNavigationView: BottomNavigationView
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
            intent.putExtra("tour_id", tourId)
            Log.d("TourActivity", "Tour ID: $tourId")
            startActivity(intent)


        }


        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->

            // Manejar las selecciones del menú
            when (menuItem.itemId) {
                R.id.nav_search -> {
                    // Abrir la actividad SearchTour si no está abierta ya
                    if (!this::class.java.simpleName.equals("SearchTour", ignoreCase = true)) {
                        startActivity(Intent(this, SearchTour::class.java))
                        finish() // Cerrar la actividad actual
                    }
                    true
                }

                R.id.nav_profile -> {
                    // Abrir la actividad Profile si no está abierta ya
                    if (!this::class.java.simpleName.equals("Profile", ignoreCase = true)) {
                        startActivity(Intent(this, Profile::class.java))
                        finish() // Cerrar la actividad actual
                    }
                    true
                }

                R.id.nav_logout -> {
                    // Abrir la actividad HomeActivity
                    logout()
                    true
                }

                else -> false
            }
        }
    }




    private fun logout() {
        // Limpiar el token de acceso al cerrar sesión
        SessionManager.clearAccessToken(this)
        // Redirigir al usuario a la pantalla de inicio de sesión
        startActivity(Intent(this, Login::class.java))
        finish() // Cerrar la actividad actual
    }
}