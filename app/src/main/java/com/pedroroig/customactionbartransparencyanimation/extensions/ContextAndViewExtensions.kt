package com.pedroroig.customactionbartransparencyanimation.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull

@Suppress("DEPRECATION")
        /**
 * Get drawable with support for older APIs
 */
fun Context.getDrawableLegacy(@NonNull id: Int): Drawable? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getDrawable(id)
        } else {
            resources.getDrawable(id)
        }

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisibilize() {
    visibility = View.INVISIBLE
}

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}
