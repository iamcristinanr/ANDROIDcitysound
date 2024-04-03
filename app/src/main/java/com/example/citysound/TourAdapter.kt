package com.example.citysound

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TourAdapter(
    //Argumento para acceder al contexto
    private val context: Context,
    //Extiende Recylerview, adapta tourList al recyclerView
    private val tourList: List<Tour>) : RecyclerView.Adapter<TourAdapter.ViewHolder>() {

    // Clase que implementa cada elemento del tour en un elemento del recycler
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textViewTourName: TextView = itemView.findViewById(R.id.textViewTourName)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val tourImageView: ImageView = itemView.findViewById(R.id.tourImageView)

    }

    // Método para implementar cada elemento del recycler en la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_possibletours, parent, false)
        return ViewHolder(view)
    }

    // Método para vincular los datos de cada elemento
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTour = tourList[position]
        //holder.imageViewTour.setImageResource(currentTour.imageTour)
        holder.textViewTourName.text = currentTour.tourName
        holder.textViewDescription.text = currentTour.description

        Glide.with(holder.itemView)
            .load(currentTour.tourImage)
            //.placeholder(R.drawable.placeholder_image) // Placeholder opcional mientras se carga la imagen
            //.error(R.drawable.error_image) // Imagen de error opcional si la carga falla
            .into(holder.tourImageView)


        holder.itemView.setOnClickListener {
            // Obtener información del tour seleccionado
            val selectedTour = tourList[position]

            // Iniciar la actividad de detalle del tour y pasar la información del tour seleccionado
            val intent = Intent(context, TourActivity::class.java)
            intent.putExtra("tourId", selectedTour.id)
            intent.putExtra("tourName", selectedTour.tourName)
            intent.putExtra("tourDescription", selectedTour.description)
            intent.putExtra("tourImage", selectedTour.tourImage)
            intent.putExtra("guideId", selectedTour.guideId)
            context.startActivity(intent)
        }
    }

    // Método para obtener la cantidad de elementos en la lista
    override fun getItemCount(): Int {
        return tourList.size
    }
}