package com.example.arvocado_android.util

import android.content.Context
import android.opengl.GLSurfaceView
import org.xml.sax.Parser

class MyGLSurfaceView(context: Context, myGLView: GLSurfaceView) : GLSurfaceView(context) {
    private val renderer : MyGLRenderer
    init {
        setEGLContextClientVersion(2)
        renderer = MyGLRenderer()
        myGLView.setRenderer(renderer)
    }
    fun initSurfaceView() {

    }
}