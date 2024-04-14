package com.example.citysound

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONException

class StopsListActivity : AppCompatActivity(), StopListAdapter.OnItemClickListener {

    //Variables mutables
    private lateinit var requestQueue: RequestQueue
    private lateinit var bottomNavigationView: BottomNavigationView
    private var tourId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerviewstops)

        requestQueue = Volley.newRequestQueue(this)

        // Configurar layout RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewStops)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // obtenemos tourId de tourActivity
        tourId = intent.getIntExtra("tour_id", -1)

        if (tourId != -1) {
            Log.d("StopsList", "tourId in onItemClick: $tourId")
            val url = "http://192.168.0.10:8000/api/tours/$tourId/stops/"
            getDataStop(url)
        } else {
            Toast.makeText(this, "Tour ID no válido $tourId", Toast.LENGTH_SHORT).show()
        }


        //BARRA DE NAVEGACION
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

    private fun logout() {
        // Limpiar el token de acceso al cerrar sesión
        SessionManager.clearAccessToken(this)
        // Redirigir al usuario a la pantalla de inicio de sesión
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Cerrar la actividad actual
    }

    private fun getDataStop(url: String) {
        //Solicitud get para obtener la lista de paradas
        val jsonArrayRequest = object : JsonArrayRequest(Method.GET, url, null,
            { response ->
                try {
                    val stops = mutableListOf<Stop>()

                    //Itera cada stopObjet de la petición
                    for (i in 0 until response.length()) {
                        val stopObject = response.getJSONObject(i)
                        Log.d("Response", "JSON Object at position $i: $stopObject")
                       //Crea objeto stop a partir datos del json
                        val stop = Stop(
                            stopObject.getInt("id"),
                            stopObject.getString("name"),
                            stopObject.getString("description"),
                            stopObject.getString("image"),

                        )
                        //Agregar stop a la lista
                        stops.add(stop)


                    }

                    Log.d("StopsList", "Stops list size: ${stops.size}")
                    // Crear y establecer el adaptador para el RecyclerView
                    val stopListAdapter = StopListAdapter(stops, this@StopsListActivity)
                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewStops)
                    recyclerView.adapter = stopListAdapter

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this@StopsListActivity, "Error al obtener la lista de paradas", Toast.LENGTH_SHORT)
                    .show()
            }) {

            // Agregar el token de autorización a las cabeceras de la solicitud
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

        // Agregar la solicitud a la cola
        requestQueue.add(jsonArrayRequest)
    }

    // Crear y establecer el adaptador para el RecyclerView
    //val stopListAdapter = StopListAdapter(getSampleStops(), this)
    //recyclerView.adapter = stopListAdapter
    //}

    // Implementar el método onItemClick del listener del adaptador
    override fun onItemClick(stop: Stop) {
        // Abrir la actividad de detalles de la parada
        val intent = Intent(this, StopActivity::class.java)
        //Enviamos los datos al StopActivity
        intent.putExtra("tourId", tourId)
        Log.d("StopsList", "tourId in onItemClick: $tourId")
        intent.putExtra("stopId", stop.id)
        intent.putExtra("stopName", stop.name)
        intent.putExtra("stopDescription", stop.description)
        intent.putExtra("stopImage", stop.image)

        startActivity(intent)
    }

}

    // Esta es una función de ejemplo que devuelve una lista de paradas de ejemplo
    /*private fun getSampleStops(): List<Stop> {
        val stops = mutableListOf<Stop>()
        // Agrega aquí las paradas que desees mostrar en la lista
        stops.add(Stop("Stop 1", "Description 1"))
        stops.add(Stop("Stop 2", "Description 2"))
        stops.add(Stop("Stop 3", "Description 3"))
        return stops*/


