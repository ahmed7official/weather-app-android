package com.android.weather.util

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


fun View.show() {
    visibility = View.VISIBLE
}//show()

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

fun View.hide(gone: Boolean = true) {

    visibility = if (gone) {
        View.GONE
    } else {
        View.INVISIBLE
    }
}//hide()

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

fun AppCompatActivity.showSnackBar(
    resId: Int,
    lengthLong: Boolean = false
) {

    val parent = window.decorView.findViewById<View>(android.R.id.content)

    Snackbar.make(
        parent,
        resId,
        Snackbar.LENGTH_SHORT.takeUnless { lengthLong } ?: Snackbar.LENGTH_LONG
    ).show()

}

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
