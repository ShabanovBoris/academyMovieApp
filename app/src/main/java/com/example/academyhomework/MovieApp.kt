package com.example.academyhomework

import android.app.Application
import com.example.academyhomework.di.AppModule
import com.example.academyhomework.di.ApplicationComponent
import com.example.academyhomework.di.DaggerApplicationComponent

class MovieApp: Application() {

   lateinit var appComponent: ApplicationComponent

    override fun onCreate() {


        appComponent = DaggerApplicationComponent.factory().create(this)

        super.onCreate()
    }


}