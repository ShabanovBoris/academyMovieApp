package com.example.academyhomework.interfaces

import androidx.fragment.app.Fragment
import java.io.Serializable

interface ScreenChangeable {
    fun moveToDetails(movie:Serializable)
    fun backFromDetails()
}