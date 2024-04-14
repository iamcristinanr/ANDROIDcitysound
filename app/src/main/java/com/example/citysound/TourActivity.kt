package com.example.citysound

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.IOException
import java.util.Timer
import java.util.TimerTask

class TourActivity : AppCompatActivity() {

    //Declaramos variables mutables
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour)

        // Obtemos los datos del intent tourAdapter
        val tourId = intent.getIntExtra("tourId", -1)
        val tourName = intent.getStringExtra("tourName")
        val description = intent.getStringExtra("tourDescription")
        val tourImage = intent.getStringExtra("tourImage")
        val guideId = intent.getIntExtra("guideId", -1)
        val duration = intent.getIntExtra("duration", -1)
        Log.d("TourActivity", "Valor de created_by: $guideId")
        Log.d("TourActivity", "Valor de tourId: $tourId")

        //Iniciamos variables inmutables
        val tourNameTextView = findViewById<TextView>(R.id.tourNameTextView)
        val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        val tourImageView = findViewById<ImageView>(R.id.tourImageView)
        val durationTextView = findViewById<TextView>(R.id.durationTour)

        //Configuramos el layout con lso datos del tour
        tourNameTextView.text = tourName
        descriptionTextView.text = description
        durationTextView.text = duration.toString()

        Glide.with(this)
            .load(tourImage)
            //.placeholder(R.drawable.placeholder_image) // Opcional: Placeholder mientras se carga la imagen
            //.error(R.drawable.error_image) // Opcional: Imagen de error si falla la carga
            .into(tourImageView)

        // Habilitar los botones
        val mapsButton = findViewById<Button>(R.id.mapsButton)
        val guideButton = findViewById<Button>(R.id.guideButton)
        val pointsOfInterestButton = findViewById<Button>(R.id.pointsOfInterestButton)
        val playTourButton = findViewById<ImageButton>(R.id.tourPlayButton)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val comButton = findViewById<Button>(R.id.comButton)

        mapsButton.isEnabled = true
        guideButton.isEnabled = true
        pointsOfInterestButton.isEnabled = true


        var isPaused = false

        //Iniciamos reproductor multimedia
        mediaPlayer = MediaPlayer()


        fun reproducirAudioTour(tourId: Int, context: Context, mediaPlayer: MediaPlayer, playTourButton: ImageButton) {
            //endpoint del tour para obtener los datos (audio)
            val url = "http://192.168.0.10:8000/api/tours/$tourId/"


            val requestQueue = Volley.newRequestQueue(context)

            // Crear una solicitud GET para obtener los detalles del tour
            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    // Procesar la respuesta JSON y obtener el audio
                    val audioUrl = response.getString("audio")

                    // Configurar el reproductor multimedia con el audio obtenido
                    try {
                        mediaPlayer.setDataSource(audioUrl)
                        mediaPlayer.prepare()
                        // Configurar la duración máxima de la seekbar con la duración del audio
                        seekBar.max = mediaPlayer.duration
                    } catch (e: IOException) {
                        Log.e("TourActivity", "Error preparando media player: ${e.message}")
                        Toast.makeText(context, "Error preparando media player", Toast.LENGTH_LONG).show()
                    }

                    // Manejar el progreso de la seekbar
                    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            if (fromUser) {
                                // Avanzar/retroceder la reproducción según el progreso de la seekbar
                                mediaPlayer.seekTo(progress)
                            }
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                    })

                    // Actualizar la posición de la seekbar mientras se reproduce el audio
                    Timer().scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            seekBar.progress = mediaPlayer.currentPosition
                        }
                    }, 0, 1000)

                    // Iniciar la reproducción
                    mediaPlayer.start()
                },
                { error ->
                    Toast.makeText(context, "Error al obtener detalles del tour: ${error.message}", Toast.LENGTH_SHORT).show()
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

            // Agregar la solicitud a la cola
            requestQueue.add(jsonObjectRequest)
        }



        playTourButton.setOnClickListener {
            if (mediaPlayer != null && !mediaPlayer.isPlaying && !isPaused) {
                // Si no se está reproduciendo y no está pausado, comenzar desde el principio
                reproducirAudioTour(tourId, this, mediaPlayer, playTourButton)
                //actualizar imagen del botón
                playTourButton.setImageResource(R.drawable.pause)
            } else if (mediaPlayer != null && !isPaused) {
                // Si no está pausado pausarlo y actualizar imagen del botón
                mediaPlayer.pause()
                playTourButton.setImageResource(R.drawable.play)
                isPaused = true
            } else  {
                // Si está pausado reanudar la reproducción
                mediaPlayer.start()
                playTourButton.setImageResource(R.drawable.pause)
                isPaused = false
            }
        }


        //BOTON PUNTOS DE INTERES STOPS
        pointsOfInterestButton.setOnClickListener {
            // Abrir la actividad de la lista de puntos de interés y pasamos id tour
            val intent = Intent(this, StopsListActivity::class.java)
            //Pasamos tourId
            intent.putExtra("tour_id", tourId)
            Log.d("TourActivity", "Tour ID: $tourId")
            startActivity(intent)
        }


            //BOTON COMENTARIOS
            // Abrir la actividad de los comentarios y pasamos id tour
        comButton.setOnClickListener {
            val intent = Intent(this, CommentListActivity::class.java)
            //Pasamos tourId
            intent.putExtra("tourId", tourId)
            startActivity(intent)
        }

            //BOTON GUIA
            //Abrir actividad profile del guia y pasar idguia
        guideButton.setOnClickListener {
            if (guideId != null) {
                val intent = Intent(this, ProfileGuide::class.java)
                //Enviamos el guideId
                intent.putExtra("guideId", guideId)
                startActivity(intent)
                Log.e("TourActivity", "guideId NO es nulo al intentar iniciar ProfileGuide $guideId")
            } else {
                Log.e("TourActivity", "guideId es nulo al intentar iniciar ProfileGuide $guideId")

            }
        }

        //BOTON MAPA
        mapsButton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
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
        }
    }


    //Metodo de la barra de navegación log out
    private fun logOut() {
        // Limpiar el token de acceso al cerrar sesión
        SessionManager.clearAccessToken(this)
        // Redirigir al usuario a la pantalla de inicio de sesión
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Cerrar la actividad actual
    }

    /*override fun onStop() {
        super.onStop()
        // Liberar recursos del MediaPlayer al salir de la actividad
        mediaPlayer.release()
    }*/


}