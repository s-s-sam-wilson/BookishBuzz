package com.example.bookishbuzz

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class BookAdapter(private val bookList: List<ItemsItem?>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val bookImageView: ImageView = itemView.findViewById(R.id.bookImageView)
        val Genre : TextView = itemView.findViewById(R.id.Genre)
        val Card : CardView = itemView.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentItem = bookList[position]?.volumeInfo
        var ISBN : String
        try {
            ISBN= currentItem?.industryIdentifiers?.get(0)?.identifier!!
        }catch(e:Exception){
            ISBN = ""
        }
        var Genre : String
        try{
            holder.Genre.text = currentItem?.categories?.get(0)
        }catch (e:Exception){

        }
        holder.Card.setOnClickListener{
            if (!ISBN.isEmpty()){
                val intent = Intent(holder.itemView.context, BookActivity::class.java)
                intent.putExtra("ISBN", ISBN);
                holder.itemView.context.startActivity(intent)
            }else{
                Toast.makeText(holder.itemView.context, "Books without ISBN Can't be viewed", Toast.LENGTH_SHORT).show()
            }

        }
        holder.titleTextView.text = currentItem?.title
        holder.authorTextView.text = currentItem?.authors?.joinToString(", ") // If there are multiple authors

        // Load image using Picasso
        currentItem?.imageLinks?.thumbnail?.let { thumbnailUrl ->
            Picasso.get().load(thumbnailUrl)
                .placeholder(R.drawable.content) // Placeholder image while loading
                .error(R.drawable.content) // Error image if loading fails
                .into(holder.bookImageView)
        }

        // Bind more data as needed
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}
