package com.example.academyhomework.adapters

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academyhomework.R
import com.example.academyhomework.models.Movie

class MovieListAdapter(val onClick: (Movie) -> Unit):ListAdapter<Movie,MovieListAdapter.ViewHolderMovie>(DiffCallback()) {


    class ViewHolderMovie(view:View):RecyclerView.ViewHolder(view){
        private val title:TextView = view.findViewById(R.id.tv_title_movie)
        private val genre:TextView = view.findViewById(R.id.tv_genre)
        private val image:ImageView = view.findViewById(R.id.iv_image_card)
        private val rating:RatingBar = view.findViewById(R.id.ratingBarCard)
        private val runtime:TextView = view.findViewById(R.id.tv_runtime)

        fun bindData(movie:Movie){
            Glide.with(itemView.context)
                .load(movie.imageUrl)
                .into(image)
            title.text = movie.title
            for(g in movie.genres){
                genre.append(g.name+" ")
            }
            rating.rating = movie.rating.toFloat()
            runtime.text = "${movie.runningTime}  min"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMovie {
        return ViewHolderMovie(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie,
        parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolderMovie, position: Int) {
            holder.bindData(getItem(position))
            holder.itemView.setOnClickListener{
                onClick(getItem(position))
            }
    }
}

class DiffCallback: DiffUtil.ItemCallback<Movie>(){
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem

}