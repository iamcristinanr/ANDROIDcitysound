package com.example.citysound

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONException

class ProfileGuide : AppCompatActivity() {

    private var guideId: Int = -1

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var nameProfileTextView: TextView
    private lateinit var bioProfileTextView: TextView
    private lateinit var photoProfileImageView: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("ProfileGuide", "guideId $guideId")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profileguide)

        //obtenemos desde TourActivity el guideId
        guideId = intent.getIntExtra("guideId", -1)

        nameProfileTextView = findViewById(R.id.nameProfile)
        bioProfileTextView = findViewById(R.id.bioProfile)
        photoProfileImageView = findViewById(R.id.photoprofile)

        //Llamamos al metodo para obtener  los datos del guia
        getGuideData()

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

    private fun logOut() {
        // Limpiar el token de acceso al cerrar sesión
        SessionManager.clearAccessToken(this)
        // Redirigir al usuario a la pantalla de inicio de sesión
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Cerrar la actividad actual
    }


    private fun getGuideData() {
        val queue = Volley.newRequestQueue(this)
        //Endpoint perfil usuario
        val url = "http://192.168.0.10:8000/api/users/$guideId"

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
                    Glide.with(this@ProfileGuide)
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
                Toast.makeText(this@ProfileGuide, "Error al obtener los datos del usuario: ${error.message}", Toast.LENGTH_LONG).show()
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

        queue.add(request)
    }
}