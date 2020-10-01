package com.example.arvocado_android.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arvocado_android.R
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }
    private fun init() {
        Timber.e("Hello")

    }
}