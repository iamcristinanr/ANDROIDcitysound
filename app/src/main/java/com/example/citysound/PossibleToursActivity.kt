package com.example.citysound

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class PossibleToursActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tourAdapter: TourAdapter
    private lateinit var tourList: MutableList<Tour>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerviewtour)

        recyclerView = findViewById(R.id.recyclerViewTours)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tourList = mutableListOf()
        tourAdapter = TourAdapter(this, tourList)
        recyclerView.adapter = tourAdapter

        val receivedData = intent.getParcelableArrayListExtra<Tour>("tourList")

        if (receivedData != null && receivedData.isNotEmpty()) {
            tourList.addAll(receivedData)
            tourAdapter.notifyDataSetChanged()
        } else {
            // Mostrar un mensaje indicando que no hay tours disponibles
            Toast.makeText(this, "No hay tours disponibles", Toast.LENGTH_SHORT).show()
        }


        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->

            // Manejar las selecciones del menú
            when (menuItem.itemId) {
                R.id.nav_search -> {
                    // Abrir la actividad SearchTour si no está abierta ya
                    if (!this::class.java.simpleName.equals("SearchTour", ignoreCase = true)) {
                        startActivity(Intent(this, SearchTourActivity::class.java))
                        finish() // Cerrar la actividad actual
                    }
                    true
                }

                R.id.nav_profile -> {
                    // Abrir la actividad Profile si no está abierta ya
                    if (!this::class.java.simpleName.equals("Profile", ignoreCase = true)) {
                        startActivity(Intent(this, ProfileActivity::class.java))
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
        //tourAdapter.notifyDataSetChanged()

        private fun logout() {
            // Limpiar el token de acceso al cerrar sesión
            SessionManager.clearAccessToken(this)
            // Redirigir al usuario a la pantalla de inicio de sesión
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Cerrar la actividad actual
    }
}