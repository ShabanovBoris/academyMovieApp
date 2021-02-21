package com.example.academyhomework.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.academyhomework.R
import com.example.academyhomework.models.Actor
import com.google.android.material.imageview.ShapeableImageView

class ActorRecyclerAdapter:RecyclerView.Adapter<ActorRecyclerAdapter.ViewHolderDataActor>() {

    private var list = listOf<Actor>()

    fun bindActors(l: List<Actor>){
        list = l
    }
    class ViewHolderDataActor(itemView: View):RecyclerView.ViewHolder(itemView){
        private var avatar:ShapeableImageView = itemView.findViewById(R.id.iv_avatar)
        private var name:TextView = itemView.findViewById(R.id.tv_actor_fullname)

        fun bindData(actor: Actor){
            avatar.setImageResource(actor.avatar)
            name.text = actor.fullName

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDataActor {
       return ViewHolderDataActor(LayoutInflater.from(parent.context)
           .inflate(R.layout.view_holder_actor,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolderDataActor, position: Int) {

        holder.bindData(list[position])
    }

    override fun getItemCount(): Int = list.size

}