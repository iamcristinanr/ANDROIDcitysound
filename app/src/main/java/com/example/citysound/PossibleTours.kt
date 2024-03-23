package com.example.citysound

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PossibleTours : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tourAdapter: TourAdapter
    private lateinit var tourList: MutableList<Tour>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerviewtour)

        recyclerView = findViewById(R.id.recyclerViewTours)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tourList = mutableListOf()
        tourAdapter = TourAdapter(this, tourList)
        recyclerView.adapter = tourAdapter

        val receivedData = intent.getParcelableArrayListExtra<Tour>("tourList")

        if (receivedData != null && receivedData.isNotEmpty()) {
            tourList.addAll(receivedData)
            tourAdapter.notifyDataSetChanged()
        } else {
            // Mostrar un mensaje indicando que no hay tours disponibles
            Toast.makeText(this, "No hay tours disponibles", Toast.LENGTH_SHORT).show()
        }

        //tourAdapter.notifyDataSetChanged()
    }
}