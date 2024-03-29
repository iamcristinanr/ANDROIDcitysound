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

class StopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stops)

        val tourId = intent.getIntExtra("tourId", -1)
        Log.d("StopsList", "tourId in stopActivity: $tourId")
        lateinit var bottomNavigationView: BottomNavigationView
        // Obtener la parada seleccionada del Intent
        val stopId = intent.getIntExtra("stopId", -1)
        val stopName = intent.getStringExtra("stopName")
        val stopDescription = intent.getStringExtra("stopDescription")
        //val stopActivity = intent.getParcelableExtra<Stop>("stop")
        val stopImage = intent.getStringExtra("stopImage")

        //val tourId = intent.getIntExtra("tourId", -1)
        // Mostrar los detalles de la parada en la interfaz de usuario
        val nameTextView = findViewById<TextView>(R.id.stopNameTextView)
        val descriptionTextView = findViewById<TextView>(R.id.stopDescriptionTextView)
        val stopImageView = findViewById<ImageView>(R.id.stopImageView)
        val playStopButton = findViewById<ImageButton>(R.id.stopPlayButton)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)

        nameTextView.text = stopName
        descriptionTextView.text = stopDescription

        Glide.with(this)
            .load(stopImage)
            //.placeholder(R.drawable.placeholder_image) // Opcional: Placeholder mientras se carga la imagen
            //.error(R.drawable.error_image) // Opcional: Imagen de error si falla la carga
            .into(stopImageView)

        val mediaPlayer = MediaPlayer()

        var isPaused = false

        fun reproducirAudioTour(tourId: Int, context: Context, mediaPlayer: MediaPlayer, playTourButton: ImageButton) {
            val url = "http://192.168.0.10:8000/api/tours/$tourId/stops/$stopId"

            // Configurar una cola de solicitudes Volley
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
                        seekBar.max = mediaPlayer.duration
                    } catch (e: IOException) {
                        Log.e("TourActivity", "Error preparing media player: ${e.message}")
                        Toast.makeText(context, "Error preparando el reproductor de audio", Toast.LENGTH_SHORT).show()
                    }

                    // Manejar el cambio de progreso en la seekbar
                    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            if (fromUser) {
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

                    // Iniciar la reproducción automáticamente
                    mediaPlayer.start()
                },
                { error ->
                    Toast.makeText(context, "Error al obtener detalles del tour: ${error.message}", Toast.LENGTH_SHORT).show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    // Obtener el token de acceso desde SessionManager
                    val sharedPreferences = context.getSharedPreferences(SessionManager.PREFS_NAME, Context.MODE_PRIVATE)
                    val accessToken = SessionManager.getAccessToken(sharedPreferences)
                    // Agregar el token de autenticación a las cabeceras si está disponible
                    accessToken?.let {
                        headers["Authorization"] = "Token $it"
                    }
                    return headers
                }


            }

            // Agregar la solicitud a la cola de solicitudes Volley
            requestQueue.add(jsonObjectRequest)
        }
        playStopButton.setOnClickListener {
            if (!mediaPlayer.isPlaying && !isPaused) {
                // Si no se está reproduciendo y no está pausado, comenzar desde el principio
                reproducirAudioTour(tourId, this, mediaPlayer, playStopButton)
                playStopButton.setImageResource(R.drawable.pause)
            } else if (!isPaused) {
                // Si no está pausado, pausarlo y actualizar el texto del botón
                mediaPlayer.pause()
                playStopButton.setImageResource(R.drawable.play)
                isPaused = true
            } else {
                // Si está pausado, reanudar la reproducción
                mediaPlayer.start()
                playStopButton.setImageResource(R.drawable.pause)
                isPaused = false
            }
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





private fun logout() {
    // Limpiar el token de acceso al cerrar sesión
    SessionManager.clearAccessToken(this)
    // Redirigir al usuario a la pantalla de inicio de sesión
    startActivity(Intent(this, Login::class.java))
    finish() // Cerrar la actividad actual
}
}