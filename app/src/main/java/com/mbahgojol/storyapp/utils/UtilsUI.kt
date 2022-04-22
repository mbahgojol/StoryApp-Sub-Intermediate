package com.mbahgojol.storyapp.utils

import android.content.res.Resources
import android.util.TypedValue
import android.widget.EditText
import kotlin.math.roundToInt

val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).roundToInt()

val Int.sp: Float
    get() = (this.dp / Resources.getSystem().displayMetrics.scaledDensity)

fun EditText.text() = text.toString()

fun Double.isNotNullOrZero() = this != 0.0