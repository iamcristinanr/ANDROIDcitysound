package com.example.citysound

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import layout.CommentListAdapter
import org.json.JSONException

class CommentListActivity : AppCompatActivity() {

    //variables mutables
    private var tourId: Int = -1 // Inicializamos tourId
    private var commentId: Int = -1
    private lateinit var requestQueue: RequestQueue
    private val commentsList = mutableListOf<Comment>()
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerviewcomments)

        // Obtenemos el tourId del TourActivity
        tourId = intent.getIntExtra("tourId", -1)

        // Creamos la cola de solicitudes
        requestQueue = Volley.newRequestQueue(this)

        //Implementamos layout recyclerview
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewComments)
        recyclerView.layoutManager = LinearLayoutManager(this)


        if (tourId != -1) {
            Log.d("CommentListActivity", "Tour ID: $tourId")

            val url = "http://192.168.0.10:8000/api/tours/$tourId/stops/"
            getComments()
        } else {
            Toast.makeText(this, "Tour ID no válido", Toast.LENGTH_SHORT).show()
        }

        //BOTON PARA DEJAR COMENTARIO
        val buttonLeaveComment = findViewById<Button>(R.id.buttonLeaveComment)
        buttonLeaveComment.setOnClickListener {
            val intent = Intent(this, LeaveCommentActivity::class.java)
            intent.putExtra("tourId", tourId)
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

    // Metodo para obtener los datos de los comentarios
    private fun getComments() {
        val url = "http://192.168.0.10:8000/api/tours/$tourId/comments/"

        val request = object : JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try{

                    for (i in 0 until response.length()) {
                        val commentObject = response.getJSONObject(i)
                        val comment = Comment(
                            commentObject.getInt("id"),
                            commentObject.getString("text"),
                            commentObject.getString("user"),
                            commentObject.getString("created_at")
                        )
                        commentsList.add(comment)
                    }

                // Crear y establecer el adaptador para el RecyclerView
                val commentAdapter = CommentListAdapter(commentsList)
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewComments)
                recyclerView.adapter = commentAdapter

            } catch (e: JSONException) {
            e.printStackTrace()
            }
        },
        { error ->
            error.printStackTrace()
            Toast.makeText(this, "Error al obtener la lista de comentarios", Toast.LENGTH_SHORT).show()
        }) {

            // Agregar el token de autorización a las cabeceras de la solicitud
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                // Obtener el token de acceso desde SessionManager
                val sharedPreferences = getSharedPreferences(SessionManager.PREFS_NAME, Context.MODE_PRIVATE)
                val accessToken = SessionManager.getAccessToken(sharedPreferences)
                // Agregar el token de autenticación a las cabeceras si está disponible
                accessToken?.let {
                    headers["Authorization"] = "Token $it"
                }
                return headers
            }
        }

        // Agregar la solicitud a la cola
        requestQueue.add(request)
    }

    private fun logOut() {
        // Limpiar el token de acceso
        SessionManager.clearAccessToken(this)
        // Redirigir al usuario a la pantalla de inicio de sesión
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Cerrar la actividad actual
    }

}


