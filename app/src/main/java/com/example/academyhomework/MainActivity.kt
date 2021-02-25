package com.example.academyhomework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.academyhomework.interfaces.ScreenChangeable
import java.io.Serializable

class MainActivity : AppCompatActivity(),ScreenChangeable {

    var fragment:FragmentMoviesDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     /** Mockup*/   if(savedInstanceState == null)
        {
         supportFragmentManager.beginTransaction()
             .addToBackStack(null)
             .replace(R.id.containerMainActivity,FragmentMovieList.newInstance("",""))
             .commit()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun moveToDetails(movie:Serializable) {
        fragment = FragmentMoviesDetails.newInstance(movie)
        supportFragmentManager.beginTransaction().apply {
            addToBackStack(null)
            add(R.id.containerMainActivity,fragment!!)
            commit()
        }
    }

    override fun backFromDetails() {
        supportFragmentManager.beginTransaction()
            .remove(fragment!!)
            .commit()
    }

}