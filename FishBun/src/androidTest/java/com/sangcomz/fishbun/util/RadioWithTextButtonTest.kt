package com.sangcomz.fishbun.util;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times

class RadioWithTextButtonTest {
    private lateinit var radioWithTextButton: RadioWithTextButton
    private lateinit var context: Context

    @Mock
    private lateinit var mockTextPaint: Paint

    @Mock
    private lateinit var mockStrokePaint: Paint

    @Mock
    private lateinit var mockCirclePaint: Paint

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = InstrumentationRegistry.getInstrumentation().context
        radioWithTextButton = RadioWithTextButton(context, mockTextPaint, mockStrokePaint, mockCirclePaint)

        doNothing().`when`(mockStrokePaint).setStrokeWidth(anyFloat())
        doNothing().`when`(mockStrokePaint).setStyle(Paint.Style.STROKE)

    }

    @After
    fun tearDown(){
    }

    @Test
    fun onDraw_drawCircle() {
        val mockCanvas = mock(Canvas::class.java)
        radioWithTextButton.draw(mockCanvas)

        verify(mockStrokePaint, Times(1)).setStyle(Paint.Style.STROKE)
        verify(mockCanvas, Times(1)).drawCircle(
                radioWithTextButton.width.toCircleXY(),
                radioWithTextButton.height.toCircleXY(),
                radioWithTextButton.width.toCircleRadio(),
                mockStrokePaint)
    }

    @Test
    fun onDraw_drawDrawable() {
        val mockDrawable = mock(Drawable::class.java)
        radioWithTextButton.setDrawable(mockDrawable)

        val mockCanvas = mock(Canvas::class.java)
        doNothing().`when`(mockCanvas).drawCircle(anyFloat(), anyFloat(), anyFloat(), any(Paint::class.java))

        radioWithTextButton.draw(mockCanvas)
        verify(mockCanvas, Times(1)).drawCircle(
                radioWithTextButton.width.toCircleXY(),
                radioWithTextButton.height.toCircleXY(),
                radioWithTextButton.width.toCircleRadio(),
                mockCirclePaint)
        verify(mockDrawable, Times(1)).setBounds(any(Rect::class.java))
        verify(mockDrawable, Times(1)).draw(mockCanvas)
    }

    @Test
    fun onDraw_drawText() {
        val text = "test"

        val mockCanvas = mock(Canvas::class.java)
        radioWithTextButton.setText(text)

        radioWithTextButton.draw(mockCanvas)
        verify(mockCanvas, Times(1)).drawCircle(
                radioWithTextButton.width.toCircleXY(),
                radioWithTextButton.height.toCircleXY(),
                radioWithTextButton.width.toCircleRadio(),
                mockCirclePaint)
        verify(mockCanvas, Times(1)).drawText(
                text,
                radioWithTextButton.width.toCircleXY(),
                radioWithTextButton.height.toCircleXY(),
                mockTextPaint)
    }

    @Test
    fun setTextSizeForWidth_doNotDraw() {
        val mockPaint = mock(Paint::class.java)
        doNothing().`when`(mockPaint).setTextSize(anyFloat())
        doNothing().`when`(mockPaint).getTextBounds(anyString(), anyInt(), anyInt(), any(Rect::class.java))
        val desiredWidth = 10f
        val text = "test"

        mockPaint.setTextSizeForWidth(text, desiredWidth)
        verify(mockPaint).setTextSize(anyFloat())
        verify(mockPaint).getTextBounds(eq(text), anyInt(), anyInt(), any(Rect::class.java))
        verifyNoMoreInteractions(mockPaint)
    }

    @Test
    fun setTextSizeForWidth_draw() {
        val mockPaint = mock(Paint::class.java)
        doNothing().`when`(mockPaint).setTextSize(anyFloat())
        doNothing().`when`(mockPaint).getTextBounds(anyString(), anyInt(), anyInt(), any(Rect::class.java))
        val desiredWidth = 1f
        val text = "test"

        mockPaint.setTextSizeForWidth(text, desiredWidth)
        verify(mockPaint).setTextSize(anyFloat())
        verify(mockPaint).getTextBounds(eq(text), anyInt(), anyInt(), any(Rect::class.java))
        verify(mockPaint).setTextSize(anyFloat())
    }

    private fun Int.toCircleXY() = (this / 2).toFloat()
    private fun Int.toCircleRadio() = (this / 3).toFloat()
}