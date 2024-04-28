package com.example.bookishbuzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentAdapter(private val comments: List<HashMap<String, Any>>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.textName)
        val textDate: TextView = itemView.findViewById(R.id.textDate)
        val textReview: TextView = itemView.findViewById(R.id.textReview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.textName.text = comment["name"] as? String ?: ""
        holder.textDate.text = comment["date"] as? String ?: ""
        holder.textReview.text = comment["review"] as? String ?: ""
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}
