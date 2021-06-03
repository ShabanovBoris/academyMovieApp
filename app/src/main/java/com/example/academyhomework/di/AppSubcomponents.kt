package com.example.academyhomework.di

import com.example.academyhomework.RouterComponent
import com.example.academyhomework.presentation.details.MovieDetailsComponent
import dagger.Module

@Module(subcomponents = [MovieDetailsComponent::class, RouterComponent::class])
class AppSubcomponents