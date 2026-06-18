package com.desktop.browser

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CursorOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val cursorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.CYAN
        alpha = 90
        style = Paint.Style.FILL
    }

    var cursorX = 300f
    var cursorY = 500f
    var enabled = true

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!enabled) return
        canvas.drawCircle(cursorX, cursorY, 24f, glowPaint)
        canvas.drawCircle(cursorX, cursorY, 6f, cursorPaint)
    }

    fun moveTo(x: Float, y: Float) {
        cursorX = x.coerceIn(0f, width.toFloat())
        cursorY = y.coerceIn(0f, height.toFloat())
        invalidate()
    }

    fun handleTouch(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                moveTo(event.x, event.y)
                return true
            }
        }
        return false
    }
}
