package com.example.citysound

import android.os.Bundle
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

        // Aquí deberías obtener los datos de los tours de la API y agregarlos a tourList
        // Por ahora, agregaremos algunos datos de ejemplo para mostrar cómo se ve

        val exampleTour1 = Tour("Tour 1", "Description of Tour 1")
        val exampleTour2 = Tour("Tour 2", "Description of Tour 2")
        //val exampleTour1 = Tour("Tour 1", "Description of Tour 1", R.drawable.tour1)
        //val exampleTour2 = Tour("Tour 2", "Description of Tour 2", R.drawable.tour2)

        tourList.add(exampleTour1)
        tourList.add(exampleTour2)

        tourAdapter.notifyDataSetChanged()
    }
}