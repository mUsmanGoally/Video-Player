package com.example.videoplayer.extentions

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(message: String, length: Int = Toast.LENGTH_LONG) {
    requireContext().toast(message, length)
}

fun Activity.toast(message: String, length: Int = Toast.LENGTH_LONG) {
    applicationContext.toast(message, length)
}

fun Context.toast(message: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(applicationContext, message, length).show()
}