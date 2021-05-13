package com.example.academyhomework.presentation.details


import com.example.academyhomework.di.scopes.DetailsScope
import dagger.Subcomponent

@DetailsScope
@Subcomponent(modules = [])
interface MovieDetailsComponent {


    fun detailsVMFactory(): DetailsViewModel.Factory

    @Subcomponent.Factory
    interface Factory{
        fun create(): MovieDetailsComponent
    }

    fun inject(fragment: FragmentMoviesDetails)
}