package layout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.citysound.Comment
import com.example.citysound.R

class CommentListAdapter (
    //Extiende Recylerview, adapta CommentListAdapter al recyclerView
    private val comments: List<Comment>) : RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>() {

    // Clase interna que representa cada elemento de la lista de comentarios
    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // variables de cada elemento de la lista que pertenece al comentario
        val userNameTextView: TextView = itemView.findViewById(R.id.userName)
        val dateCommentTextView: TextView = itemView.findViewById(R.id.dateComment)
        val commentTextView: TextView = itemView.findViewById(R.id.comment)
    }

    //crea un nuevo viewholder (elemento de la lista-comentario)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            //implementa el diseño layout
            .inflate(R.layout.activity_listcomments, parent, false)
        return CommentViewHolder(view)
    }

    //Asocia los datos de un comentario a un viewholder
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        //obtiene los datos de la posición
        val currentItem = comments[position]
        //asigna los datos a la vista holder correspondiente
        holder.userNameTextView.text = currentItem.user
        holder.dateCommentTextView.text = currentItem.date
        holder.commentTextView.text = currentItem.text
    }

    override fun getItemCount(): Int {
        return comments.size
    }


}