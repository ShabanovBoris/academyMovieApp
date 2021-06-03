package com.example.academyhomework.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.academyhomework.R
import com.example.academyhomework.entities.Movie
import com.google.android.material.card.MaterialCardView


open class MovieListAdapter(private val onClick: (Int, View) -> Unit) :
    ListAdapter<Movie, MovieListAdapter.ViewHolderMovie>(
        DiffCallback()
    ) {

    companion object {
        const val DEFAULT = 100
        const val FOOTER = 111
    }

    override fun getItemCount(): Int {
        return if (super.getItemCount() != 0) {
            super.getItemCount() + 1
        } else {
            return 0
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position + 1 == itemCount) {
            FOOTER
        } else {
            DEFAULT
        }
    }

    open class ViewHolderMovie(view: View) : RecyclerView.ViewHolder(view)
    class ViewHolderFooter(view: View) : ViewHolderMovie(view)
    class ViewHolderMovieItem(view: View) : ViewHolderMovie(view) {
        private val title: TextView = view.findViewById(R.id.tv_title_movie)
        private val genre: TextView = view.findViewById(R.id.tv_genre)
        private val image: ImageView = view.findViewById(R.id.iv_image_card)
        private val rating: RatingBar = view.findViewById(R.id.ratingBarCard)
        private val release: TextView = view.findViewById(R.id.tv_release)
        private val card: MaterialCardView = view.findViewById(R.id.movieCard)


        fun bindData(movie: Movie, onClick: (Int, View) -> Unit) {
            image.load(movie.imageUrl) {
                allowHardware(false)
                crossfade(true)
                placeholder(R.drawable.ic_loading_image)
            }

            ViewCompat.setTransitionName(card, movie.id.toString())


            title.text = movie.title

            genre.text = ""
            for (g in movie.genres) {
                genre.append(g.name + " ")
            }
            rating.rating = movie.rating.toFloat()
            release.text = movie.releaseDate

            setClickListener(movie, onClick)
        }

        private fun setClickListener(movie: Movie, onClick: (Int, View) -> Unit) {
            itemView.setOnClickListener { onClick(movie.id, card) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMovie {

        return when (viewType) {
            DEFAULT -> ViewHolderMovieItem(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_holder_movie,
                    parent, false
                )
            )
            FOOTER -> ViewHolderFooter(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.footer_holder_movie,
                    parent, false
                )
            )
            else -> throw IllegalStateException("$viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolderMovie, position: Int) {
        when (holder) {
            is ViewHolderMovieItem -> holder.bindData(getItem(position), onClick)
        }

//            holder.itemView.setOnClickListener{
//                onClick(getItem(position).id, holder.itemView.findViewById(R.id.movieCard))
//            }
    }


}

class DiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem

}