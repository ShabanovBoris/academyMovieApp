package com.example.academyhomework.services.schedule_movie_work_manager

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.academyhomework.BaseFragment
import com.example.academyhomework.Router
import com.example.academyhomework.utils.DatePickerFragment
import com.example.academyhomework.utils.TimePickerFragment

class ScheduleMovieTimePicker() {
    companion object {
        @SuppressLint("NewApi")
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