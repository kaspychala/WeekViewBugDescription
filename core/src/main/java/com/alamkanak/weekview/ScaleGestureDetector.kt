package com.alamkanak.weekview

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector as AndroidScaleGestureDetector

internal class ScaleGestureDetector(
    context: Context,
    private val viewState: ViewState,
    private val valueAnimator: ValueAnimator,
    private val onInvalidation: () -> Unit
) {

    private val listener = object : AndroidScaleGestureDetector.OnScaleGestureListener {

        override fun onScaleBegin(
            detector: AndroidScaleGestureDetector
        ): Boolean = !valueAnimator.isRunning

        override fun onScale(detector: AndroidScaleGestureDetector): Boolean {
            val hourHeight = viewState.hourHeight
            viewState.newHourHeight = hourHeight * detector.scaleFactor
            onInvalidation()
            return true
        }

        override fun onScaleEnd(detector: AndroidScaleGestureDetector) {
            onInvalidation()
        }
    }

    private val detector = AndroidScaleGestureDetector(context, listener)

    fun onTouchEvent(event: MotionEvent) {
        if (!valueAnimator.isRunning && !viewState.showCompleteDay) {
            detector.onTouchEvent(event)
        }
    }
}
