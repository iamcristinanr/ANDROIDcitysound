package com.example.citysound

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
import java.util.Calendar

class LeaveCommentActivity : AppCompatActivity() {

    //variables mutables
    private var tourId: Int = -1

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leavecomment)

        requestQueue = Volley.newRequestQueue(this)

        val sendButton = findViewById<Button>(R.id.confirmButton)
        val editComment = findViewById<EditText>(R.id.commentEditText)

        //Obtenemos tourId para construir la url
        tourId = intent.getIntExtra("tourId", -1)

        if (tourId == -1) {
            Toast.makeText(this, "Tour ID no válido", Toast.LENGTH_SHORT).show()
        }

        // Boton que envia el comentario a la api
        sendButton.setOnClickListener {
            val commentText = editComment.text.toString()

            if (commentText.isNotEmpty()) {
                sendComments(commentText)
                val intent = Intent(this, CommentListActivity::class.java)
                //volvemos a enviar el idtour** mejorar
                intent.putExtra("tourId", tourId)
                startActivity(intent)
                finish() // Cerrar la actividad actual
            } else {
                Toast.makeText(this, "Por favor ingrese un comentario", Toast.LENGTH_SHORT).show()
            }
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


    private fun logOut() {
        // Limpiar el token de acceso
        SessionManager.clearAccessToken(this)
        // Redirigir al usuario a la pantalla de inicio de sesión
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Cerrar la actividad actual
    }

    //Metodo construye la url
    private fun sendComments(commentText: String) {
        val url = "http://192.168.0.10:8000/api/tours/$tourId/comments/"

        val sharedPreferences = getSharedPreferences(SessionManager.PREFS_NAME, Context.MODE_PRIVATE)
        val user = SessionManager.getUser(sharedPreferences)

        val commentJson = JSONObject().apply {
            put("user", 2) // modificar api (funciona con el token, no es necesario)
            put("text", commentText)
            put("tour", tourId)

            Log.d("SendCommentParams", "User: $user, Text: $commentText, Tour ID: $tourId")
        }

        //metodo post
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, commentJson,
            { response ->
                // mensaje exito
                Toast.makeText(this, "Comentario enviado con éxito", Toast.LENGTH_SHORT).show()
            },
            { error ->
                // mensaje error
                Toast.makeText(this, "Error al enviar el comentario: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {

            // Agregar el token de autorización a las cabeceras de la solicitud
            override fun getHeaders(): MutableMap<String, String> {
                // Encabezado de la solicitud http
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

        requestQueue.add(jsonObjectRequest)
    }
}




