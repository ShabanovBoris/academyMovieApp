package com.example.academyhomework.presentation.playing_list

import androidx.lifecycle.ViewModel
import com.example.academyhomework.domain.data.MovieDatabase
import com.example.academyhomework.domain.data.MovieNetwork

class OnPlayingMoviesViewModel(
    private val movieDatabase: MovieDatabase,
    private val movieNetwork: MovieNetwork,
): ViewModel() {





//
// TODO 1. move paging logic here
//      in MainViewModel keep only cache logic
//      2. network query in MainActivity move to OnPlayingFragment
//      3. add Router in parameter constructor
//

    //TODO implement pattern MVI

     
    //TODO Activity shouldn't talk VM when to load data


}