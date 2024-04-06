package com.example.citysound

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import org.json.JSONObject
import android.Manifest
import android.app.Activity

class EditProfileActivity : AppCompatActivity() {

    //variables mutables
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var requestQueue: RequestQueue
    private var userurl: String = ""
    //private lateinit var photoProfile: ShapeableImageView
    //private lateinit var buttonChangePhoto: ImageButton

    //codigo de solicitud abrir galeria
    //private val GALLERY_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        requestQueue = Volley.newRequestQueue(this)

        //asignar datos a los elementos del layout
        //photoProfile = findViewById(R.id.photoprofile)
        //val editPhotoProfile= findViewById<ImageButton>(R.id.buttonChangePhoto)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextBio = findViewById<EditText>(R.id.editTextBio)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        // Obtener la URL, es un parametro del usuario para actualizar el perfil
        getUrl()


        // Boton que cambia la foto de perfil
        /*editPhotoProfile.setOnClickListener {
            // Solicitar permisos de almacenamiento externo si no están concedidos
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    GALLERY_REQUEST_CODE
                )
            } else {
                openGallery()
            }
        }*/



        // Boton que guarda los datos
        buttonSave.setOnClickListener {
            val newUsername = editTextUsername.text.toString()
            val newBio = editTextBio.text.toString()

            // Verificar si se obtuvo el ID del usuario correctamente
            if (userurl == "") {
                Toast.makeText(this, "No se pudo obtener el Url del usuario", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }

            // Crear el objeto JSON con los datos actualizados
            val params = JSONObject()

            params.put("name", newUsername)
            params.put("biography", newBio)

            // Crear la solicitud PUT que envia los datos para actualizar la API
            val request = object : JsonObjectRequest(
                Request.Method.PATCH, userurl, params,
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

            // BARRA DE NAVEGACION - MEJORAR
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
                    // Abrir la actividad HomeActivity
                    logOut()
                    true
                }
                else -> false
            }

        }
    }


// METODO PARA OBTENER LA URL DEL PERFIL LOGADO
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

        requestQueue.add(request)
    }

    //Metodo de la barra de navegación log out
    private fun logOut() {
        // Limpiar el token de acceso
        SessionManager.clearAccessToken(this)
        // Redirigir al usuario a la pantalla de inicio de sesión
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Cerrar la actividad actual
    }
/*
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                photoProfile.setImageURI(uri)


                val realPath = RealPathUtil.getRealPathFromURI(this, uri)
                if (realPath != null) {

                    // uploadImage(realPath)
                } else {
                    Toast.makeText(
                        this,
                        "No se pudo obtener la ruta de la imagen",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }*/
        }


