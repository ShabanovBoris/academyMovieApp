package com.example.academyhomework


import com.example.academyhomework.view.launcher.LaunchFragment
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
        fun create(@BindsInstance router: Router): RouterComponent
    }

    fun inject(act: MainActivity)
    fun inject(frag: LaunchFragment)
}