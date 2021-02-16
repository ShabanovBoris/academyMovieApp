package com.example.academyhomework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.academyhomework.interfaces.ScreenChangeable

class MainActivity : AppCompatActivity(),ScreenChangeable {
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

    override fun moveTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            addToBackStack(null)
            replace(R.id.containerMainActivity,fragment)
            commit()
        }
    }

}