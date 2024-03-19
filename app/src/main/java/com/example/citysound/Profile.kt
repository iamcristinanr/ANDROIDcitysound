package com.example.citysound

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import android.content.SharedPreferences

class Profile: AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sharedPreferences: SharedPreferences


    private fun logout() {
        // Limpiar el estado de inicio de sesión en SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        // Redirigir al usuario a la pantalla de inicio de sesión
        startActivity(Intent(this, Login::class.java))
        finish() // Cerrar la actividad actual
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }

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
}
