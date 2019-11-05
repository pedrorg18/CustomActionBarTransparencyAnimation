package com.pedroroig.customactionbartransparencyanimation.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View


/**
 * Fades a view by progressively reducing its opacity
 */
fun fadeAnimation(view: View, duration: Long): ObjectAnimator =
        ObjectAnimator.ofFloat(view, "alpha", 0f).apply {
        this.duration = duration
    }

/**
 * Un-fades a view by progressively increasing its opacity
 */
fun unFadeAnimation(view: View, duration: Long): ObjectAnimator =
    ObjectAnimator.ofFloat(view, "alpha", 1f).apply {
        this.duration = duration
    }

/**
 * Changes a View's width from initial to final value
 * @param duration of the animation, in milliseconds
 * @param onAnimStart action to perform before running the animation
 */
fun changeWidthAnimation(view: View, duration: Long, initWidth: Int, finalWidth: Int,
                         onAnimStart: () -> Unit = {}): ValueAnimator =
        ValueAnimator.ofInt(initWidth, finalWidth).apply {
            this.duration = duration
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    onAnimStart()
                }
            })
            addUpdateListener { valueAnimator ->
                val currentValue = valueAnimator!!.animatedValue as Int
                setViewWidth(view, currentValue)
            }

        }
