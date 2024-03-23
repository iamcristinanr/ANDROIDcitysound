package com.example.citysound
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            // Manejar las selecciones del menú
            when (menuItem.itemId) {
                R.id.nav_search -> {
                    // No hacer nada si ya estamos en la actividad SearchTour
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
                    // Cerrar sesión
                    logout()
                    true
                }
                else -> false
            }
        }
    }

    private fun searchTours(city: String, tourName: String, guideName: String) {
        // URL de la API para realizar la búsqueda
        val apiUrl = "http://192.168.0.10:8000/api/tours/?location=$city&name=$tourName&created_by=$guideName"




        // Crear una solicitud JSON Array usando Volley
        val request = object : JsonArrayRequest(
            Method.GET, apiUrl, null,
            { response ->
                try {
                    // Manejar la respuesta de la API aquí
                    val tourList = mutableListOf<Tour>()

                    for (i in 0 until response.length()) {
                        val tourObject = response.getJSONObject(i)
                        val id = tourObject.getInt("id")
                        val tourName = tourObject.getString("name")
                        val description = tourObject.getString("description")
                        // Aquí puedes obtener más atributos del objeto tour si es necesario


                        val tour = Tour(id, tourName, description)
                        tourList.add(tour)
                    }

                    Log.d("SearchTours", "URL de la API: $apiUrl")
                    Log.d("SearchTours", "Parámetros de búsqueda - Ciudad: $city, Nombre del tour: $tourName, Nombre del guía: $guideName")

                    // Pasar la lista de tours a la actividad PossibleTours
                    val intent = Intent(this, PossibleTours::class.java)
                    intent.putParcelableArrayListExtra("tourList", ArrayList(tourList))
                    startActivity(intent)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                // Manejar errores de la solicitud HTTP
                Toast.makeText(this, "Error en la búsqueda: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {

            // Sobreescribir el método getHeaders() para incluir el token de autenticación en las cabeceras de la solicitud
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                // Obtener el token de acceso desde SessionManager
                val sharedPreferences = getSharedPreferences(SessionManager.PREFS_NAME, Context.MODE_PRIVATE)
                val accessToken = SessionManager.getAccessToken(sharedPreferences)
                // Agregar el token de autenticación a las cabeceras si está disponible
                accessToken?.let {
                    headers["Authorization"] = "Token $it"
                }
                return headers
            }
        }

        // Agregar la solicitud a la cola de solicitudes de Volley para que se ejecute
        Volley.newRequestQueue(this).add(request)
    }

    private fun logout() {
        // Limpiar el token de acceso al cerrar sesión
        SessionManager.clearAccessToken(this)
        // Redirigir al usuario a la pantalla de inicio de sesión
        startActivity(Intent(this, Login::class.java))
        finish() // Cerrar la actividad actual
    }


}