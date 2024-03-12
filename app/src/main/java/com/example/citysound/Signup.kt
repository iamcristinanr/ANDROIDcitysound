package com.example.citysound

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Signup: AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    //onCreate cuando se invoca por primera vez una activity(frontend)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


    }
}