package com.example.academyhomework.di


import android.content.Context
import com.example.academyhomework.RouterComponent
import com.example.academyhomework.di.scopes.AppScope
import com.example.academyhomework.view.playing_list.OnPlayingMovieFragment
import com.example.academyhomework.view.details.MovieDetailsComponent
import com.example.academyhomework.view.search.SearchFragment
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
    fun inject(fragment: OnPlayingMovieFragment)
    fun inject(fragment: SearchFragment)

    //inject background
    fun inject(worker: UpdateDBWorker)
    fun inject(worker: ScheduleNotificationWorker)

    //subcomponents
    fun plusDetailsComponent(): MovieDetailsComponent.Factory
    fun plusRouterComponent(): RouterComponent.Factory
}