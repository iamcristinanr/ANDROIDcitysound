package com.example.citysound

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class EditProfile : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        requestQueue = Volley.newRequestQueue(this)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextBio = findViewById<EditText>(R.id.editTextBio)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        // Obtener el ID del usuario del servidor antes de enviar la solicitud de actualización del perfil
        getUserId()

        buttonSave.setOnClickListener {
            val newUsername = editTextUsername.text.toString()
            val newBio = editTextBio.text.toString()

            // Verificar si se obtuvo el ID del usuario correctamente
            if (userId == -1) {
                Toast.makeText(this, "No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Construir la URL de la API para actualizar el perfil con el ID del usuario
            val url = "http://192.168.0.10:8000/api/users/$userId/"

            // Crear el objeto JSON con los datos actualizados
            val params = JSONObject()
            params.put("name", newUsername)
            params.put("biography", newBio)

            // Crear la solicitud PUT
            val request = object : JsonObjectRequest(
                Request.Method.PUT, url, params,
                Response.Listener { response ->
                    // Manejar la respuesta de la API
                    Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT)
                        .show()
                    // Puedes agregar más lógica aquí, como redirigir a otra actividad
                },
                Response.ErrorListener { error ->
                    // Manejar errores de la solicitud
                    Toast.makeText(
                        this,
                        "Error al actualizar el perfil: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {

                // Incluir encabezados de autorización u otros encabezados necesarios
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    // Obtener el token de acceso desde SharedPreferences
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

            // Agregar la solicitud a la cola de solicitudes
            requestQueue.add(request)
        }
    }

    private fun getUserId() {
        val url = "http://192.168.0.10:8000/api/users/{id}"

        val request = object : JsonObjectRequest(Method.GET, url, null,
            { response ->
                try {
                    userId = response.getInt("id")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                // Obtener el token de acceso desde SharedPreferences
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

        requestQueue.add(request)
    }
}
