package com.example.presentation.ext

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

internal fun ExtendedFloatingActionButton.shrinkFabIfPossible() {
    if (this.isExtended)
        this.shrink()
}

internal fun ExtendedFloatingActionButton.extendFabIfPossible() {
    if (!this.isExtended)
        this.extend()
}

internal fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

internal fun Activity.hideKeyboard() {
    if (currentFocus == null) View(this) else currentFocus?.let { hideKeyboard(it) }
}

internal fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}