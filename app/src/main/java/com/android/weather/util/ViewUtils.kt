package com.android.weather.util

import android.view.View


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
