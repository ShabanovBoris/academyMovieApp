package com.example.academyhomework.utils

import android.util.Log
import androidx.work.WorkInfo
import com.example.academyhomework.presentation.MainViewModelMovie

object WorkManagerHelper {

    fun statesHandler(workInfo: WorkInfo): Boolean =
        when (workInfo.state) {
            WorkInfo.State.RUNNING -> {
                Log.d(MainViewModelMovie.TAG, "WorkInfo.State.RUNNING")
                false
            }
            WorkInfo.State.ENQUEUED -> {
                Log.d(MainViewModelMovie.TAG, "WorkInfo.State.ENQUEUED")
                true
            }
            WorkInfo.State.FAILED -> {
                Log.d(MainViewModelMovie.TAG, "WorkInfo.State.FAILED")
                false
            }
            else -> {
                Log.e(MainViewModelMovie.TAG, "WorkInfo.State.ELSE BRANCH")
                false
            }
        }
}

