package com.example.citysound
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONException

class SearchTour : AppCompatActivity() {
    private lateinit var cityEditText: EditText
    private lateinit var tourNameEditText: EditText
    private lateinit var guideNameEditText: EditText
    private lateinit var searchButton: Button

    private lateinit var bottomNavigationView: BottomNavigationView

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
            searchTours(city, tourName, guideName)
        }
    }

    private fun searchTours(city: String, tourName: String, guideName: String) {
        // URL de la API para realizar la búsqueda
        val apiUrl = "https://tu-api.com/search?city=$city&tourName=$tourName&guideName=$guideName"

        // Crear una solicitud JSON Array usando Volley
        val request = JsonArrayRequest(
            Request.Method.GET, apiUrl, null,
            Response.Listener<JSONArray> { response ->
                // Manejar la respuesta de la API aquí
                // Por ejemplo, puedes procesar los datos de la respuesta y pasarlos a la actividad TourActivity
                val intent = Intent(this, TourActivity::class.java)
                // Puedes pasar los datos de la respuesta como extras en el intent si es necesario
                startActivity(intent)
            },
            Response.ErrorListener { error ->
                // Manejar errores de la solicitud HTTP
                Toast.makeText(this, "Error en la búsqueda: ${error.message}", Toast.LENGTH_SHORT).show()
            })

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
                    startActivity(Intent(this, SearchTour::class.java))
                    true
                }

                else -> false
            }
        }
        // Agregar la solicitud a la cola de solicitudes de Volley para que se ejecute
        Volley.newRequestQueue(this).add(request)
    }
}