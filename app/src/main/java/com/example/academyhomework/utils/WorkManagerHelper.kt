package com.example.academyhomework.utils

import android.util.Log
import androidx.work.WorkInfo
import com.example.academyhomework.presentation.playing_list.PlayingListViewModelMovie

object WorkManagerHelper {

    fun statesHandler(workInfo: WorkInfo): Boolean =
        when (workInfo.state) {
            WorkInfo.State.RUNNING -> {
                Log.d(PlayingListViewModelMovie.TAG, "WorkInfo.State.RUNNING")
                false
            }
            WorkInfo.State.ENQUEUED -> {
                Log.d(PlayingListViewModelMovie.TAG, "WorkInfo.State.ENQUEUED")
                true
            }
            WorkInfo.State.FAILED -> {
                Log.d(PlayingListViewModelMovie.TAG, "WorkInfo.State.FAILED")
                false
            }
            else -> {
                Log.e(PlayingListViewModelMovie.TAG, "WorkInfo.State.ELSE BRANCH")
                false
            }
        }
}

