package com.example.citysound

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
//import com.android.volley.toolbox
import org.json.JSONException
import org.json.JSONObject

class LoginActivity: AppCompatActivity() {
    //variables mutables
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    //onCreate cuando se invoca por primera vez una activity(frontend)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) //Implementa el layout

        //Elementos del layout
        emailEditText = findViewById(R.id.emailLogin)
        passwordEditText = findViewById(R.id.passwordLogin)

        // Variables para el cambio de activity
        val loginButton: Button = findViewById(R.id.buttonLogin)
        val textViewSignUp: TextView = findViewById(R.id.notRegistered)
        val textViewReset: TextView = findViewById(R.id.resetPassword)

        // Agregar un OnClickListener a los TextViews Singup y forgotPassword
        textViewSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
        textViewReset.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        // Agregamos escuchador al loginButton
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
                // Llamamos al método login y le pasamos los parametros del escuchador
            login(email, password)
        }

    }

    //Metodo inicio de sesión
    private fun login(email: String, password: String) {
        //Endpoint login api
        val apiUrl = "http://192.168.0.10:8000/auth-token/"

        // Preparar json para enviar los datos para solicitud https
        val jsonBody = JSONObject() //Constructor biblioteca Android
        //corregir en la api es username pero deberia ser email
        jsonBody.put("username", email)
        jsonBody.put("password", password)

        //Verificamos si el cuerpo json por logs es correcto
        Log.d("Request JSON", jsonBody.toString())

        // Se envia una peticion Post VOLLEY
        val request = JsonObjectRequest(
            Request.Method.POST, apiUrl, jsonBody,

            // Respuesta Api
            { response ->
                try {
                    //guardamos "token"
                    val accessToken = response.getString("token")
                    // Verificar si el token de acceso es válido
                    if (accessToken.isNotEmpty()) {
                        // Guardar el token de acceso - CLASE MANEJO DE TOCKEN SesionManager
                        SessionManager.saveAccessToken(this, accessToken)
                        //Ver token por pantalla
                        Log.d("Token", "El token de acceso es: $accessToken")
                        // Iniciar la siguiente actividad Profile
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Mostrar mensaje de error al usuario si no se recibió un token válido
                        Toast.makeText(this, "Token de acceso no válido", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Json incorrecto.", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Credenciales incorrectas. Inténtalo de nuevo", Toast.LENGTH_LONG).show()
            }
        )

        //Creamos cola solicitud Volley
        Volley.newRequestQueue(this).add(request)
        }
    }


