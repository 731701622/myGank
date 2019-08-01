package com.wkx.common.utils

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun <T> T.logd(tag: String = "zz") {
    when (this) {
        is List<*> -> {
            for (i in this) {
                i.logd()
            }
        }
        else -> Log.d(tag, this.toString())
    }
}

fun Activity.toast(msg: String? = null, resId: Int? = null) {
    msg?.also {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    } ?: resId?.also {
        Toast.makeText(this, getString(it), Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.toast(msg: String? = null, @StringRes resId: Int? = null) {
    msg?.also {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    } ?: resId?.also {
        Toast.makeText(context, getString(it), Toast.LENGTH_SHORT).show()
    }
}