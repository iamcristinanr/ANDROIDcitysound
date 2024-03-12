package com.example.citysound

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
//import com.android.volley.toolbox
import org.json.JSONException
import org.json.JSONObject

class Login: AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    //onCreate cuando se invoca por primera vez una activity(frontend)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailLogin)
        passwordEditText = findViewById(R.id.passwordLogin)
//escuchador del boton, agregamos los campos que se guardan y donde.
        val loginButton: Button = findViewById(R.id.buttonLogin)
        val textViewSignUp: TextView = findViewById(R.id.notRegistered)
        val textViewReset: TextView = findViewById(R.id.resetPassword)

        // Agregar un OnClickListener al TextView
        textViewSignUp.setOnClickListener {
            // Navegar a la actividad de registro
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        textViewReset.setOnClickListener {
            // Navegar a la actividad de registro
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
//invocamos a la funcion perfomLogin agregada más abajo.
                // Realizar el inicio de sesión
            performLogin(email, password)
        }

    }


    private fun performLogin(email: String, password: String) {
        val apiUrl = "https://api.escuelajs.co/api/v1/auth/login"


//VERIFICAR hacer conversiones de string a JSON que le pasamos a la API
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        val request = JsonObjectRequest(
            Request.Method.POST, apiUrl, jsonBody,
            { response ->
                try {
                    val accessToken = response.getString("access_token")
                    // Guardar el token de acceso
                    SessionManager.saveAccessToken(this, accessToken)
                    Log.d("Token", "El token de acceso es: $accessToken")
                    // El inicio de sesión fue exitoso
                    // AQUI HABRIA QUE Redirigir a la siguiente actividad, etc.redirectToMainActivity()//ActivityProfile(ver)

                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Error al analizar la respuesta JSON de la API
                }
            },
            { error ->
                error.printStackTrace()
                // Manejar errores de la solicitud HTTP
            }
        )

        Volley.newRequestQueue(this).add(request)
        }
    }


