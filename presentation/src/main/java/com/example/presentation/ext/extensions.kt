package com.example.presentation.ext

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

internal fun ExtendedFloatingActionButton.shrinkFabIfPossible(){
    if(this.isExtended)
        this.shrink()
}

internal fun ExtendedFloatingActionButton.extendFabIfPossible(){
    if(!this.isExtended)
        this.extend()
}
