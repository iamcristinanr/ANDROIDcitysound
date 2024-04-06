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


class SearchTourActivity : AppCompatActivity() {
    //Declaramos variables mutables
    private lateinit var cityEditText: EditText
    private lateinit var tourNameEditText: EditText
    private lateinit var guideNameEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchtour)
        //Obtenemos los datos del layout
        cityEditText = findViewById(R.id.editTextCity)
        tourNameEditText = findViewById(R.id.editTextTourName)
        guideNameEditText = findViewById(R.id.editTextGuideName)
        searchButton = findViewById(R.id.buttonSearch)

        //Preparamos datos inmutables una vez presionado el boton
        searchButton.setOnClickListener {
            val city = cityEditText.text.toString()
            val tourName = tourNameEditText.text.toString()
            val guideName = guideNameEditText.text.toString()

            // Metodo de búsqueda en la API
            searchTours(city, tourName, guideName)
        }

        //-BARRA DE NAVEGACION
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            // Seleccion barra de navegación
            when (menuItem.itemId) {
                R.id.nav_search -> {
                    // No hacer nada si ya estamos en la actividad SearchTour
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

        //JSON Array usando Volley
        val request = object : JsonArrayRequest(
            Method.GET, apiUrl, null,
            { response ->
                try {
                    // Manejamos respuesta de la API
                    val tourList = mutableListOf<Tour>()
                        // Recorremos cada objeto
                    for (i in 0 until response.length()) {
                        //Extraemos los datos de la api
                        val tourObject = response.getJSONObject(i)
                        val tourId = tourObject.getInt("id")
                        val tourName = tourObject.getString("name")
                        val description = tourObject.getString("description")
                        val tourImage = tourObject.getString("photo")
                        val guideId = tourObject.getInt("created_by")
                        val duration = tourObject.getInt("duration")



                        val tour = Tour(tourId, tourName, description, tourImage, guideId, duration)
                        tourList.add(tour)
                    }

                    Log.d("SearchTours", "URL de la busqueda API: $apiUrl")
                    Log.d("SearchTours", "Parámetros de búsqueda - Ciudad: $city, Nombre del tour: $tourName, Nombre del guía: $guideName")

                    // Pasamos la lista de tours a la actividad PossibleTours
                    val intent = Intent(this, PossibleToursActivity::class.java)
                    intent.putParcelableArrayListExtra("tourList", ArrayList(tourList))
                    startActivity(intent)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                // ERROR de la solicitud HTTP
                Toast.makeText(this, "Error en la búsqueda: ${error.message}", Toast.LENGTH_LONG).show()
            }) {

            //SOBREESCRIBIMOS EL METODO GETHEADERS (DE VOLLEY) PARA ENVIAR EL TOKEN DESDE SESION MANAGER
            override fun getHeaders(): MutableMap<String, String> {
                //Encabezado de la solicitud http
                val headers = HashMap<String, String>()
                // Obtener el token de acceso desde SessionManager
                val sharedPreferences = getSharedPreferences(SessionManager.PREFS_NAME, Context.MODE_PRIVATE)
                val accessToken = SessionManager.getAccessToken(sharedPreferences)
                // Agregar el token de autenticación a la cabecera si tenemos token (puede ser nulo)
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
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Cerrar la actividad actual
    }


}