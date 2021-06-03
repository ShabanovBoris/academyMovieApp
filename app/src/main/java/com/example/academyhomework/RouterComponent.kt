package com.example.academyhomework


import dagger.BindsInstance
import dagger.Subcomponent

/**
 * subcomponent only for new injections, at least onPlayingViewModel
 * in earlie versions use old interface
 */
@Subcomponent(modules = [])
interface RouterComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance instance: Router): RouterComponent
    }

    fun inject(act: MainActivity)
}