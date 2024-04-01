package com.example.citysound

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.imageview.ShapeableImageView
import org.json.JSONException
import com.bumptech.glide.Glide


class ProfileActivity: AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var nameProfileTextView: TextView

    private lateinit var bioProfileTextView: TextView

    private lateinit var photoProfileImageView: ShapeableImageView



        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        nameProfileTextView = findViewById(R.id.nameProfile)
        bioProfileTextView = findViewById(R.id.bioProfile)
        photoProfileImageView = findViewById(R.id.photoprofile)





        val buttonEditProfile = findViewById<Button>(R.id.buttonEditProfile)
        val buttonSearchTour = findViewById<Button>(R.id.buttonSearchTour)


        getUserProfileData()


        buttonEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Configurar el botón para ir a la actividad SearchTour
        buttonSearchTour.setOnClickListener {
            val intent = Intent(this, SearchTourActivity::class.java)
            startActivity(intent)
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

    private fun getUserProfileData() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.0.10:8000/api/users/me/"

    val request = object : JsonObjectRequest(Method.GET, url, null,
        { response ->
            // Manejar la respuesta de la API aquí
            try {
                val name = response.getString("name")
                val biography = response.getString("biography")
                val photoprofile = response.getString("picture")

                nameProfileTextView.text = name
                bioProfileTextView.text = biography

                Glide.with(this@ProfileActivity)
                    .load(photoprofile)
                    .circleCrop()
                    .into(photoProfileImageView)
            } catch (e: JSONException) {
                e.printStackTrace()
                // Manejar el caso en que la respuesta no tenga el formato esperado
            }
        },
        { error ->
            Toast.makeText(this@ProfileActivity, "Error al obtener los datos del usuario: ${error.message}", Toast.LENGTH_SHORT).show()
            Log.d("ProfileActivity", "Error al obtener los datos del usuario: ${error.message}")
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
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Cerrar la actividad actual
    }
}
