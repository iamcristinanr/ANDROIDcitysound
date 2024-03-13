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
import org.json.JSONArray
import org.json.JSONException

class SearchTour : AppCompatActivity() {
    private lateinit var cityEditText: EditText
    private lateinit var tourNameEditText: EditText
    private lateinit var guideNameEditText: EditText
    private lateinit var searchButton: Button

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

        // Agregar la solicitud a la cola de solicitudes de Volley para que se ejecute
        Volley.newRequestQueue(this).add(request)
    }
}