package com.example.academyhomework

import androidx.fragment.app.Fragment
import java.io.Serializable

interface Router {
    fun moveToDetails(movie:Serializable)
    fun backFromDetails()
}