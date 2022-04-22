package com.mbahgojol.storyapp.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import com.mbahgojol.storyapp.R
import com.mbahgojol.storyapp.utils.sp

class EditTextPassword : AppCompatEditText {
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
        textError = context.getString(R.string.error_password)

        mPaintTextError = Paint(Paint.FAKE_BOLD_TEXT_FLAG)
            .apply {
                textSize = 30.sp
                color = Color.RED
                this.alpha = 0
            }

        rectError = Rect()

        doOnTextChanged { txt, _, _, _ ->
            mPaintTextError?.alpha = if (txt?.length ?: 0 < 6) 255 else 0
        }
    }

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