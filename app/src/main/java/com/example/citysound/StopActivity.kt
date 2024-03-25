package com.example.citysound

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView

class StopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stops)

        lateinit var bottomNavigationView: BottomNavigationView
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