package com.example.citysound

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class SignupActivity: AppCompatActivity() {
    //Declaramos variables mutables
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var password1EditText: EditText
    private lateinit var password2EditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        nameEditText = findViewById(R.id.name)
        emailEditText = findViewById(R.id.email)
        password1EditText = findViewById(R.id.password1)
        password2EditText = findViewById(R.id.password2)

        //Variable button inmutable
        val signUpButton: Button = findViewById(R.id.button)
        //Escuchador
        signUpButton.setOnClickListener {
            //Varaibles vuelve inmutables cuando pulsa botón
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password1 = password1EditText.text.toString()
            val password2 = password2EditText.text.toString()

            // Realizar el registro
            signUp(name, email, password1, password2)
        }
    }

    private fun signUp(name: String, email: String, password1: String, password2: String) {
        val apiUrl = "http://192.168.0.10:8000/register/"

        // Crear cuerpo json con las variables obtenidas en los edittext
        val jsonBody = JSONObject()
        jsonBody.put("username", name)
        jsonBody.put("email", email)
        jsonBody.put("password1", password1)
        jsonBody.put("password2", password2)

        // Petición de la API
        val request = JsonObjectRequest(
            Request.Method.POST, apiUrl, jsonBody,
            { response ->
                try {
                    Toast.makeText(this, "Registro exitoso, verifica tu correo para entrar en tu cuenta", Toast.LENGTH_LONG).show()
                    finish() // Terminar la actividad de registro después del registro exitoso
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error en la solicitud contacte con el administrador", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error en el registro VERIFICA LAS CREDENCIALES", Toast.LENGTH_LONG).show()
            }
        )

        //Poner la petición a la cola
        Volley.newRequestQueue(this).add(request)
    }
}