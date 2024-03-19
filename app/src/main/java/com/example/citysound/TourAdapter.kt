package com.example.citysound

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TourAdapter(private val context: Context, private val tourList: List<Tour>) :
    RecyclerView.Adapter<TourAdapter.ViewHolder>() {

    // Clase ViewHolder que representa cada elemento de la lista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val imageViewTour: ImageView = itemView.findViewById(R.id.imageViewTour)
        val textViewTourName: TextView = itemView.findViewById(R.id.textViewTourName)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        // Agrega aquí otras vistas si es necesario
    }

    // Método para inflar el diseño de cada elemento de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_possibletours, parent, false)
        return ViewHolder(view)
    }

    // Método para vincular los datos de cada elemento con las vistas correspondientes
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTour = tourList[position]
        //holder.imageViewTour.setImageResource(currentTour.imageTour)
        holder.textViewTourName.text = currentTour.tourName
        holder.textViewDescription.text = currentTour.description
        // Agrega aquí la lógica para vincular otros datos si es necesario
    }

    // Método para obtener la cantidad de elementos en la lista
    override fun getItemCount(): Int {
        return tourList.size
    }
}