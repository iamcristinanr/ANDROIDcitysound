package com.example.citysound

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class Profile: AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var nameProfileTextView: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        nameProfileTextView = findViewById(R.id.nameProfile)


        val buttonEditProfile = findViewById<Button>(R.id.buttonEditProfile)
        val buttonSearchTour = findViewById<Button>(R.id.buttonSearchTour)

        getUserProfileData()


        buttonEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        // Configurar el botón para ir a la actividad SearchTour
        buttonSearchTour.setOnClickListener {
            val intent = Intent(this, SearchTour::class.java)
            startActivity(intent)
        }


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
                    logout()
                    true
                }

                else -> false
            }
        }

    }

    private fun getUserProfileData() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.0.10:8000/api/users/"

        val request = object : JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                // Manejar la respuesta de la API aquí
                try {
                    if (response.length() > 0) {
                        val userObject = response.getJSONObject(0)
                        val name = userObject.getString("name")

                        nameProfileTextView.text = name
                    }else{
                        Toast.makeText(this, "No se encontraron datos de usuario", Toast.LENGTH_SHORT).show()
                    }
                    // Aquí puedes mostrar otros datos del usuario según sea necesario
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Manejar el caso en que la respuesta no tenga el formato esperado
                }
            },
            { error ->
                Toast.makeText(this, "Error al obtener los datos del usuario: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.d("ProfileActivity","Error al obtener los datos del usuario: ${error.message}")
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

        queue.add(request)
    }

    private fun logout() {
        // Limpiar el token de acceso al cerrar sesión
        SessionManager.clearAccessToken(this)
        // Redirigir al usuario a la pantalla de inicio de sesión
        startActivity(Intent(this, Login::class.java))
        finish() // Cerrar la actividad actual
    }
}
