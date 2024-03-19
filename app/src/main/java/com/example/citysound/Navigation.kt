package com.example.citysound

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.citysound.databinding.ActivityNavigationBinding

class Navigation : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el listener para el NavigationView
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Manejar las selecciones del menú
            when (menuItem.itemId) {
                R.id.nav_search -> {
                    // Abrir la actividad HomeActivity
                    startActivity(Intent(this, SearchTour::class.java))
                    true
                }
                R.id.nav_profile -> {
                    // Abrir la actividad HomeActivity
                    startActivity(Intent(this, Profile::class.java))
                    true
                }
                R.id.nav_logout -> {
                    // Abrir la actividad HomeActivity
                    startActivity(Intent(this, SearchTour::class.java))
                    true
                }

                else -> false
            }
        }
    }
}