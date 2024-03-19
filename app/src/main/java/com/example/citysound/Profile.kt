package com.example.citysound

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Profile: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)



        val buttonEditProfile = findViewById<Button>(R.id.buttonEditProfile)
        val buttonSearchTour = findViewById<Button>(R.id.buttonSearchTour)
        val nameProfileTextView = findViewById<TextView>(R.id.nameProfile)

        val username = intent.getStringExtra("username")
        nameProfileTextView.text = username

        buttonEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
        }

        // Configurar el botón para ir a la actividad SearchTour
        buttonSearchTour.setOnClickListener {
            val intent = Intent(this, SearchTour::class.java)
            startActivity(intent)
        }
    }
}
