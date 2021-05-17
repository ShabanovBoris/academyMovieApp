package com.example.academyhomework.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.util.*

class DatePickerFragment(private val viewContext: Context) : DialogFragment(), DatePickerDialog.OnDateSetListener {
    var years: Int = 0
    var months: Int = 0
    var days: Int = 0

    fun showWithClass(manager: FragmentManager, tag: String?) : DialogFragment{
        super.show(manager, tag)
        return this
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(viewContext, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        years = year
        months = month
        days = day
    }
}