package com.example.academyhomework

import android.view.View
import androidx.fragment.app.Fragment
import java.io.Serializable

interface Router {
    fun moveToDetails(movie:Serializable)
    fun backFromDetails()
}