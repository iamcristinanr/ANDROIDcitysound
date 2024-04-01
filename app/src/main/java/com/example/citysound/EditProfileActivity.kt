package com.example.citysound

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import org.json.JSONObject

class EditProfileActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var requestQueue: RequestQueue

    private var userurl: String = ""

    private lateinit var photoProfile: ShapeableImageView

    private lateinit var buttonChangePhoto: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        requestQueue = Volley.newRequestQueue(this)

        val photoProfile = findViewById<ShapeableImageView>(R.id.photoprofile)
        val editPhotoProfile= findViewById<ImageButton>(R.id.buttonChangePhoto)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextBio = findViewById<EditText>(R.id.editTextBio)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        // Obtener el ID del usuario del servidor antes de enviar la solicitud de actualización del perfil
        getUrl()



        buttonSave.setOnClickListener {
            val newUsername = editTextUsername.text.toString()
            val newBio = editTextBio.text.toString()

            // Verificar si se obtuvo el ID del usuario correctamente
            if (userurl == "") {
                Toast.makeText(this, "No se pudo obtener el Url del usuario", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Construir la URL de la API para actualizar el perfil con el ID del usuario
            val url = userurl

            // Crear el objeto JSON con los datos actualizados
            val params = JSONObject()
            params.put("name", newUsername)
            params.put("biography", newBio)

            // Crear la solicitud PUT
            val request = object : JsonObjectRequest(
                Request.Method.PUT, userurl, params,
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



    private fun getUrl() {
        val url = "http://192.168.0.10:8000/api/users/me/"

        val request = object : JsonObjectRequest(Method.GET, url, null,
            { response ->
                try {
                    userurl = response.getString("url")
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
    private fun logout() {
        // Limpiar el token de acceso al cerrar sesión
        SessionManager.clearAccessToken(this)
        // Redirigir al usuario a la pantalla de inicio de sesión
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Cerrar la actividad actual
    }


}