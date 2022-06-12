package com.untungs.weatherapp.common

import java.text.DateFormat
import java.util.*

fun Long.toDateString(dateFormat: Int = DateFormat.FULL): String {
    val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
    return df.format(this)
}

typealias Function = () -> Unit