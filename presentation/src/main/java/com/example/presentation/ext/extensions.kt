package com.example.presentation.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

internal fun ExtendedFloatingActionButton.shrinkFabIfPossible() {
    if (this.isExtended)
        this.shrink()
}

internal fun ExtendedFloatingActionButton.extendFabIfPossible() {
    if (!this.isExtended)
        this.extend()
}

internal fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}