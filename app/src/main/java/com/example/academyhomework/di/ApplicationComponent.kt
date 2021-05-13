package com.example.academyhomework.di


import android.content.Context
import androidx.work.Worker
import com.example.academyhomework.MainActivity
import com.example.academyhomework.di.scopes.AppScope
import com.example.academyhomework.presentation.FragmentMovieList
import com.example.academyhomework.presentation.details.MovieDetailsComponent
import com.example.academyhomework.services.db_update_work_manager.UpdateDBWorker
import com.example.academyhomework.services.schedule_movie_work_manager.ScheduleNotificationWorker
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [RetrofitModule::class, AppModule::class, AppSubcomponents::class, RoomModule::class])
interface ApplicationComponent {


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    //inject views
    fun inject(fragment: FragmentMovieList)
    fun inject(act: MainActivity)
    //inject background
    fun inject(work: UpdateDBWorker)
    fun inject(work: ScheduleNotificationWorker)

    //subcomponents
    fun plusDetailsComponent(): MovieDetailsComponent.Factory
}