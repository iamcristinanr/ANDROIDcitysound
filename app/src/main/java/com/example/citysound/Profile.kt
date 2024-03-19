package com.example.citysound

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Profile: AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)



        val buttonEditProfile = findViewById<Button>(R.id.buttonEditProfile)
        val buttonSearchTour = findViewById<Button>(R.id.buttonSearchTour)
        val nameProfileTextView = findViewById<TextView>(R.id.nameProfile)

        val username = intent.getStringExtra("username")
        nameProfileTextView.text = username

        buttonEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        // Configurar el botón para ir a la actividad SearchTour
        buttonSearchTour.setOnClickListener {
            val intent = Intent(this, SearchTour::class.java)
            startActivity(intent)
        }

        /*navigationView.setNavigationItemSelectedListener { menuItem ->
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
        }*/
    }
}
