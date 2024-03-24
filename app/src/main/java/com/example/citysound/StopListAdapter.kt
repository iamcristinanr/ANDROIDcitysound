package com.example.citysound

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StopListAdapter(
    private val stopActivities: List<Stop>,
    private val listener: OnItemClickListener) : RecyclerView.Adapter<StopListAdapter.ViewHolder>() {

    interface OnItemClickListener {

        fun onItemClick(stop: Stop)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val nameTextView: TextView = itemView.findViewById(R.id.stopNameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.stopDescriptionTextView)
        private val stopImageView: ImageView = itemView.findViewById(R.id.stopImageView)



        init {
            itemView.setOnClickListener(this)
        }

        fun bind(stopActivity: Stop) {
            nameTextView.text = stopActivity.name
            descriptionTextView.text = stopActivity.description

            Glide.with(itemView)
                .load(stopActivity.image)
                //.placeholder(R.drawable.placeholder_image) // Placeholder opcional mientras se carga la imagen
                //.error(R.drawable.error_image) // Imagen de error opcional si la carga falla
                .into(stopImageView)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedItem = stopActivities[position]
                listener.onItemClick(clickedItem)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_liststops, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = stopActivities[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return stopActivities.size
    }
}