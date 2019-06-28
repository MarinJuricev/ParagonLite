package com.example.presentation.ui.history

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.example.presentation.R
import kotlinx.android.synthetic.main.history_dialog_fragment.*
import java.util.*


class HistoryFragmentDialog(val listener: HistoryCalendarListener) : DialogFragment() {

    interface HistoryCalendarListener {
        fun onDateRangeSelected(startDate: String, endDate: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // request a window without the title
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.history_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()
    }

    private fun bindUI() {
        calendar.setCalendarListener(object : DateRangeCalendarView.CalendarListener {
            override fun onFirstDateSelected(startDate: Calendar) {}

            override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
                listener.onDateRangeSelected(startDate.time.toString(), endDate.time.toString())
            }
        })
    }

}