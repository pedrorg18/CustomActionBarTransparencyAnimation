package com.pedroroig.customactionbartransparencyanimation.utils

import android.view.View

/**
 * Sets the width to the view. IMPORTANT: this won't work in every layout phase, it should be called
 * after layout has been done.
 */
fun setViewWidth(view: View, width: Int) {
    view.layoutParams.apply {
        this.width = width
    }.let {
        view.layoutParams = it
    }
}