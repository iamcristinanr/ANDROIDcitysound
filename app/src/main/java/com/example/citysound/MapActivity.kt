package com.example.citysound

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            // Configurar el mapa
            configurarMapa(googleMap)
        }

    }

    private fun configurarMapa(googleMap: GoogleMap) {
        // Configurar el tipo de mapa
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Añadir marcadores de puntos de interés
        agregarMarcador(googleMap, 40.4170, -3.7139, "Palacio Real")
        agregarMarcador(googleMap, 40.4240, -3.7179, "Templo de Debod")



        val builder = LatLngBounds.Builder()

        // Agregar cada marcador al builder
        builder.include(LatLng(40.4170, -3.7139)) // Palacio Real
        builder.include(LatLng(40.4240, -3.7179))
        val bounds = builder.build()

        // Configurar la cámara para que muestre los límites
        val padding = 100 // Puedes ajustar el relleno según tus necesidades
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        googleMap.animateCamera(cameraUpdate)
    }
}

    private fun agregarMarcador(googleMap: GoogleMap, latitud: Double, longitud: Double, titulo: String) {
        val ubicacion = LatLng(latitud, longitud)
        googleMap.addMarker(MarkerOptions().position(ubicacion).title(titulo))
    }
