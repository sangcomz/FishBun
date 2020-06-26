package com.sangcomz.fishbun.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting

import com.sangcomz.fishbun.R

/**
 * Created by sangcomz on 01/05/2017.
 */

class RadioWithTextButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    constructor(context: Context, textPaint: Paint, strokePaint: Paint, circlePaint: Paint) : this(context) {
        this.textPaint = textPaint
        this.strokePaint = strokePaint
        this.circlePaint = circlePaint
    }

    private var radioType: RadioType = RadioType.None

    private var textPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isFakeBoldText = true
    }
    private var strokePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var circlePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val textWidth: Float
        get() = (width / 3) * 2 - PADDING_TEXT

    private val centerRect: Rect
        get() = _centerRect!!

    private var _centerRect: Rect? = null
        get() {
            if (field == null) {
                val r = Rect(0, 0, width, height)
                val width = width / 4
                field = Rect((r.exactCenterX() - width).toInt(), (r.exactCenterY() - width).toInt(), getWidth() - width, height - width)
            }
            return field
        }

    override fun onDraw(canvas: Canvas) {
        strokePaint.strokeWidth = (width / 18).toFloat()
        isSelected {
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 3).toFloat(), circlePaint)
        }
        with(radioType) {
            isRadioText {
                drawTextCentered(canvas, textPaint, text, (width / 2).toFloat(), (height / 2).toFloat())
            } isRadioDrawable {
                drawable.bounds = centerRect
                drawable.draw(canvas)
            } isRadioNone {
                strokePaint.style = Paint.Style.STROKE
                canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 3).toFloat(), strokePaint)
            }
        }
    }

    fun setTextColor(@ColorInt color: Int) = textPaint.run { this.color = color }

    fun setCircleColor(@ColorInt color: Int) = circlePaint.run { this.color = color }

    fun setStrokeColor(@ColorInt color: Int) = strokePaint.run { this.color = color }

    fun setText(text: String) {
        radioType = RadioType.RadioText(text)
        invalidate()
    }

    fun setDrawable(drawable: Drawable) {
        radioType = RadioType.RadioDrawable(drawable)
        invalidate()
    }

    fun unselect() {
        radioType = RadioType.None
        invalidate()
    }

    private fun isSelected(block: () -> Unit) = if (radioType != RadioType.None) block() else Unit

    private fun drawTextCentered(canvas: Canvas, paint: Paint, text: String, cx: Float, cy: Float) {
        val textBounds = Rect()
        with(paint) {
            setTextSizeForWidth(text, textWidth)
            getTextBounds(text, 0, text.length, textBounds)
        }
        canvas.drawText(text, cx - textBounds.exactCenterX(), cy - textBounds.exactCenterY(), paint)
    }

    private fun fetchAccentColor(): Int {
        val typedValue = TypedValue()
        val a = context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorAccent))
        val color = a.getColor(0, 0)
        a.recycle()
        return color
    }

    companion object {
        private const val PADDING_TEXT = 20.toFloat()
    }
}
