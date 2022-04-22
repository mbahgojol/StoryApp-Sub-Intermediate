package com.mbahgojol.storyapp.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Patterns
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import com.mbahgojol.storyapp.R
import com.mbahgojol.storyapp.utils.sp

class EditTextEmail : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private var mPaintTextError: Paint? = null
    private var rectError: Rect? = null
    private var textError: String = ""

    private fun init() {
        textError = context.getString(R.string.error_email)

        mPaintTextError = Paint(Paint.FAKE_BOLD_TEXT_FLAG)
            .apply {
                textSize = 30.sp
                color = Color.RED
                this.alpha = 0
            }

        rectError = Rect()

        doAfterTextChanged {
            mPaintTextError?.alpha = if (it.toString().isValidEmail()) 0 else 255
        }
    }

    private fun String.isValidEmail() =
        !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaintTextError?.getTextBounds(textError, 0, textError.length, rectError)
        mPaintTextError?.let {
            canvas.drawText(
                textError,
                paddingLeft.toFloat(),
                height.toFloat(),
                it
            )
        }
    }
}