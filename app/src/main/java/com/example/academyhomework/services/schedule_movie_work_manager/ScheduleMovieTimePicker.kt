package com.example.academyhomework.services.schedule_movie_work_manager

import com.example.academyhomework.view.BaseFragment
import com.example.academyhomework.utils.DatePickerFragment
import com.example.academyhomework.utils.TimePickerFragment

class ScheduleMovieTimePicker() {
    companion object {

        fun schedule(fragment: BaseFragment, movieId: Int) {
            /**     in UI
             * --->[Date] picking first
             * ---> [Time] picking second
             * ---> finally call in [Time] callback[ScheduleMovieWorkRepository]*/
            var date = DatePickerFragment(fragment.requireContext())
            TimePickerFragment().apply {
                setAfterDoneAction {
                    ScheduleMovieWorkRepository(
                        appContext = fragment.requireContext().applicationContext,
                        movieId = movieId,
                        time = this,
                        date = date
                    ).start()
                }
            }.show(fragment.requireActivity().supportFragmentManager, "tag")

            date = date.showWithClass(
                fragment.requireActivity().supportFragmentManager,
                "tag"
            ) as DatePickerFragment
        }
    }

}