package com.example.citysound

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

//Mejor metodo que tourAdapter?¿

class StopListAdapter(
    private val stopActivities: List<Stop>, // Lista de paradas
    private val listener: OnItemClickListener //maneja clics en los elementos de la lista
) : RecyclerView.Adapter<StopListAdapter.ViewHolder>() {

    //interfaz para clics
    interface OnItemClickListener {

        fun onItemClick(stop: Stop)
    }

    //Class view holder para los elementos de la lista
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val nameTextView: TextView = itemView.findViewById(R.id.stopNameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.stopDescriptionTextView)
        private val stopImageView: ImageView = itemView.findViewById(R.id.stopImageView)



        init {
            itemView.setOnClickListener(this) //los view holders son oyentes
        }

        //vincula los datos de las stops con la vista
        fun bind(stopActivity: Stop) {
            nameTextView.text = stopActivity.name
            descriptionTextView.text = stopActivity.description

            Glide.with(itemView)
                .load(stopActivity.image)
                //.placeholder(R.drawable.placeholder_image) // Placeholder opcional mientras se carga la imagen
                //.error(R.drawable.error_image) // Imagen de error opcional si la carga falla
                .into(stopImageView)
        }

        //manega clics
        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedItem = stopActivities[position]
                listener.onItemClick(clickedItem)
            }


        }
    }

    //implementa el diseño en cada elemento de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_liststops, parent, false)
        return ViewHolder(view)
    }

    //vincula los atos a una posición
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = stopActivities[position]
        holder.bind(currentItem)
    }
    // Método para obtener la cantidad de elementos en la lista
    override fun getItemCount(): Int {
        return stopActivities.size
    }
}