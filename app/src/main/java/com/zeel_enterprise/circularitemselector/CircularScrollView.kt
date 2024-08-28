package com.zeel_enterprise.circularitemselector

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class CircularScrollView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 1f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val iconBitmaps: List<Bitmap> = listOf(
        BitmapFactory.decodeResource(resources, R.drawable.ic_camera),
        BitmapFactory.decodeResource(resources, R.drawable.ic_video),
        BitmapFactory.decodeResource(resources, R.drawable.ic_slow_motion),
        BitmapFactory.decodeResource(resources, R.drawable.ic_panorama),
        BitmapFactory.decodeResource(resources, R.drawable.ic_setting)
    )

    private val arcRect = RectF()
    private var scrollOffset = 0f

    init {
        isClickable = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = (width / 2).toFloat() + 60f
        arcRect.set(
            -60f, height.toFloat() - radius, width.toFloat() + 60f, height.toFloat()
        )

        canvas.drawArc(arcRect, 180F, 180F, false, paint)

        drawIcons(canvas, radius)
    }

    private fun drawIcons(canvas: Canvas, radius: Float) {
        val centerX = width / 2f
        val centerY = height.toFloat()

        val angles = listOf(-60f, 0f, 60f)

        val desiredIconSize = 100

        val dotPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        for ((index, angle) in angles.withIndex()) {
            val radian = Math.toRadians((angle + scrollOffset).toDouble())
            val iconX = (centerX + radius * cos(radian)).toFloat()
            val iconY = (centerY + radius * sin(radian)).toFloat()

            val dotRadius = 10f
            canvas.drawCircle(iconX, iconY, dotRadius, dotPaint)

            val originalBitmap =
                iconBitmaps[(index + ((scrollOffset / 60).toInt() + iconBitmaps.size) % iconBitmaps.size)]

            val scaledBitmap =
                Bitmap.createScaledBitmap(originalBitmap, desiredIconSize, desiredIconSize, true)

            canvas.drawBitmap(
                scaledBitmap,
                iconX - scaledBitmap.width / 2,
                iconY - scaledBitmap.height / 2,
                null
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Handle initial touch event
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                // Calculate the amount of horizontal scroll
                val dx = event.x - width / 2f
                scrollOffset += dx / 10 // Adjust scroll sensitivity
                invalidate() // Redraw the view
                return true
            }

            MotionEvent.ACTION_UP -> {
                // Handle the touch release event
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}
