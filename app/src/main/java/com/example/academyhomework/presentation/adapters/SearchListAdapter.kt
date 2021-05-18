package com.example.academyhomework.presentation.adapters

import android.view.View
import android.widget.ListAdapter

class SearchListAdapter(private val onClick: (Int, View) -> Unit): MovieListAdapter(onClick) {

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return DEFAULT
    }
}