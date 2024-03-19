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
                // Realizar el inicio de sesión
            performLogin(email, password)
        }

    }


    private fun performLogin(email: String, password: String) {
        val apiUrl = "http://192.168.0.17:8000/auth-token/"


//VERIFICAR hacer conversiones de string a JSON que le pasamos a la API
        val jsonBody = JSONObject()
        //corregir en la api es username pero deberia ser email
        jsonBody.put("username", email)
        jsonBody.put("password", password)

        Log.d("Request JSON", jsonBody.toString())

        val request = JsonObjectRequest(
            Request.Method.POST, apiUrl, jsonBody,


            { response ->
                try {
                    val accessToken = response.getString("token")
                    // Verificar si el token de acceso es válido
                    if (accessToken.isNotEmpty()) {
                        // Guardar el token de acceso
                        SessionManager.saveAccessToken(this, accessToken)
                        Log.d("Token", "El token de acceso es: $accessToken")
                        // Iniciar la siguiente actividad
                        val intent = Intent(this, Profile::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Mostrar mensaje de error si no se recibió un token válido
                        Toast.makeText(this, "Token de acceso no válido", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Credenciales incorrectas. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error de conexión. Inténtalo de nuevo más tarde.", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(request)
        }
    }


