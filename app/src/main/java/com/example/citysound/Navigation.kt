package com.example.citysound

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class Navigation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)


        // Configurar el listener para el NavigationView
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            // Manejar las selecciones del menú
            when (menuItem.itemId) {
                R.id.nav_search -> {
                    // Abrir la actividad HomeActivity
                    startActivity(Intent(this, SearchTourActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    // Abrir la actividad HomeActivity
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.nav_logout -> {
                    // Abrir la actividad HomeActivity
                    startActivity(Intent(this, SearchTourActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }
}