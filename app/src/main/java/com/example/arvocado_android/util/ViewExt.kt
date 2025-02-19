package com.example.arvocado_android.util

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun View.setRound(curveRadius: Float) {
    outlineProvider = object : ViewOutlineProvider() {

        override fun getOutline(view: View?, outline: Outline?) {
            outline?.setRoundRect(1.dpToPx(), 1.dpToPx(), view!!.width, view.height, curveRadius)
        }
    }
    clipToOutline = true
}