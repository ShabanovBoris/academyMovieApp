package com.example.academyhomework.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.inputmethod.InputMethodManager
import com.example.academyhomework.databinding.FragmentSearchBinding
import com.example.academyhomework.view.search.SearchFragment


val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInputFromWindow(windowToken, InputMethodManager.SHOW_FORCED, 0)
}

fun SearchFragment.hideEditText(binding: FragmentSearchBinding){

   binding.etSearching.animate().translationY(-500F)
        .setDuration(1000L)
        .start()


    binding.searchingBackground.animate().translationY(-500F)
        .setDuration(1000L)
        .start()

}
fun SearchFragment.showEditText(binding: FragmentSearchBinding) {
    binding.etSearching.animate().translationY(0F)
        .setDuration(1000L)
        .setInterpolator(AnticipateOvershootInterpolator())
        .start()



    binding.searchingBackground.animate().translationY(0F)
        .setDuration(1000L)
        .start()
}
