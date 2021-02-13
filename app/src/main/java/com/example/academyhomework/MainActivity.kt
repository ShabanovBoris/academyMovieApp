package com.example.academyhomework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     /** Mockup*/   startActivity(Intent(this, MovieDetailsActivity::class.java))

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}