package com.example.arvocado_android.util

import android.view.View
import android.view.animation.AlphaAnimation

fun View.fadeIn(mills: Int) {
    AlphaAnimation(0f, 1f).apply {
        duration = mills.toLong()
    }.run { this@fadeIn.animation = this }
}

fun View.fadeOut(mills: Int) {
    AlphaAnimation(1f, 0f).apply {
        duration = mills.toLong()
    }.run { this@fadeOut.animation = this }
}

fun View.fadeInWithVisible(mills: Int) {
    AlphaAnimation(0f, 1f).apply {
        duration = mills.toLong()
    }.run { this@fadeInWithVisible.animation = this }
    this.setVisible()
}

fun View.fadeOutWithGone(mills: Int) {
    AlphaAnimation(1f, 0f).apply {
        duration = mills.toLong()
    }.run { this@fadeOutWithGone.animation = this }
    this.setGone()
}