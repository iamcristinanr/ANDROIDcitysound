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
    // Declaramos variables mutables
    private lateinit var nameProfileTextView: TextView
    private lateinit var bioProfileTextView: TextView
    private lateinit var photoProfileImageView: ShapeableImageView
    private lateinit var bottomNavigationView: BottomNavigationView

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        //Obtenemos los datos del layout
        nameProfileTextView = findViewById(R.id.nameProfile)
        bioProfileTextView = findViewById(R.id.bioProfile)
        photoProfileImageView = findViewById(R.id.photoprofile)

         //Inicializamos variables inmutables
        val buttonEditProfile = findViewById<Button>(R.id.buttonEditProfile)
        val buttonSearchTour = findViewById<Button>(R.id.buttonSearchTour)

        //Llamamos al metodo obtiene los datos del usuario de la API
        getUserProfileData()

        //BOTON ACCEDER EDITPROFILE
        buttonEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        //BOTTON PARA ACCEDER SEARCHTOUR
        buttonSearchTour.setOnClickListener {
            val intent = Intent(this, SearchTourActivity::class.java)
            startActivity(intent)
        }

        //BARRA DE NAVEGACION - MEJORAR
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->

            // Seleccion barra de navegación
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
                    // Cerrar sesión
                    logOut()
                    true
                }

                else -> false
            }
        } //BARRA DE NAVEGACION - MEJORAR

    }

    private fun getUserProfileData() {
        //Iniciamos petición
        val queue = Volley.newRequestQueue(this)
        //Endpoint de la API
        val url = "http://192.168.0.10:8000/api/users/me/"

    val request = object : JsonObjectRequest(Method.GET, url, null,
        { response ->
            // Manejamos respuesta de la API
            try {
                //Una vez que obtengamos al respuesta variables inmutables
                val name = response.getString("name")
                val biography = response.getString("biography")
                val photoprofile = response.getString("picture")

                //Añadimos infomación de la api a la vista layout
                nameProfileTextView.text = name
                bioProfileTextView.text = biography

                //Añadimos imagen GLIDE CARGA DESDE URL OJO ADMINISTRA CACHÉ
                Glide.with(this@ProfileActivity)
                    .load(photoprofile)
                    .circleCrop()
                    .into(photoProfileImageView)
            } catch (e: JSONException) {
                e.printStackTrace()
                Toast.makeText(this, "Formato de datos no válido para mostrar", Toast.LENGTH_LONG).show()
                // Manejar el caso en que la respuesta no tenga el formato esperado
            }
        },
        { error ->
            Toast.makeText(this@ProfileActivity, "Error al obtener los datos del usuario: ${error.message}", Toast.LENGTH_LONG).show()
            Log.d("ProfileActivity", "Error al obtener los datos del usuario: ${error.message}")
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

        //Añadimos peticion a la cola creada
        queue.add(request)
    }

    //Metodo de la barra de navegación log out
    private fun logOut() {
        // Limpiar el token de acceso
        SessionManager.clearAccessToken(this)
        // Redirigir al usuario a la pantalla de inicio de sesión
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Cerrar la actividad actual
    }
}
