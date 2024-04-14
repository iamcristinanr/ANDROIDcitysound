package com.example.citysound

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONException

class MapActivity : AppCompatActivity() {

    //variables mutables
    private var tourId: Int = -1 // Inicializamos tourId
    private val stopsCoordinatesList = mutableListOf<Pair<Double, Double>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        tourId = intent.getIntExtra("tourId", -1)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            // Configurar el mapa
            configurarMapa(googleMap)
        }

        getCoordenatesStop(tourId)

    }


    private fun configurarMapa(googleMap: GoogleMap) {
        // Configurar el tipo de mapa
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

    }
        private fun agregarMarcador(
            googleMap: GoogleMap,
            latitud: Double,
            longitud: Double,
            titulo: String
        ) {
            val ubicacion = LatLng(latitud, longitud)
            googleMap.addMarker(MarkerOptions().position(ubicacion).title(titulo))
        }


    private fun getCoordenatesStop(tourId: Int) {
        //Solicitud get para obtener coordenates
        val url = "http://192.168.0.10:8000/api/tours/$tourId/stops/"

        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = object : JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    // Limpiar la lista de coordenadas antes de agregar nuevas coordenadas
                    stopsCoordinatesList.clear()

                    // Procesar la respuesta JSON para obtener las coordenadas de las paradas
                    for (i in 0 until response.length()) {
                        val stopObject = response.getJSONObject(i)
                        val latitude = stopObject.getDouble("location_latitude")
                        val longitude = stopObject.getDouble("location_longitude")

                        // Agregar las coordenadas a la lista
                        stopsCoordinatesList.add(Pair(latitude, longitude))

                        // Aquí puedes hacer lo que necesites con las coordenadas
                        // Por ejemplo, agregarlas a una lista o mostrarlas en un mapa
                        Log.d(
                            "Coordenadas",
                            "Parada $i - Latitud: $latitude, Longitud: $longitude"
                        )
                    }

                    // Después de obtener todas las coordenadas, actualizar el mapa con las nuevas paradas
                    actualizarMapaConParadas()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(
                    this,
                    "Error al obtener las coordenadas de las paradas",
                    Toast.LENGTH_SHORT
                ).show()
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                // Obtener el token de acceso desde SessionManager
                val sharedPreferences =
                    getSharedPreferences(SessionManager.PREFS_NAME, Context.MODE_PRIVATE)
                val accessToken = SessionManager.getAccessToken(sharedPreferences)
                // Agregar el token de autenticación a las cabeceras si está disponible
                accessToken?.let {
                    headers["Authorization"] = "Token $it"
                }
                return headers
            }
        }

        requestQueue.add(jsonArrayRequest)
    }


    private fun actualizarMapaConParadas() {
        // Verificar si el mapa ya está listo
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            // Limpiar todos los marcadores anteriores en el mapa
            googleMap.clear()

            // Agregar marcadores para cada parada en la lista de coordenadas
            for ((index, coordinates) in stopsCoordinatesList.withIndex()) {
                agregarMarcador(
                    googleMap,
                    coordinates.first,
                    coordinates.second,
                    "Parada $index"
                )
            }

            // Crear un builder de límites para incluir todos los marcadores
            val builder = LatLngBounds.Builder()
            for (coord in stopsCoordinatesList) {
                builder.include(LatLng(coord.first, coord.second))
            }
            val bounds = builder.build()

            // Configurar la cámara para que muestre los límites con un margen de 100dp
            val padding = 100 // Puedes ajustar el relleno según tus necesidades
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            googleMap.animateCamera(cameraUpdate)
        }

    }
}