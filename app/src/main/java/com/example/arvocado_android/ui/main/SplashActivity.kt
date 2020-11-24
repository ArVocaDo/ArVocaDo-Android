package com.example.arvocado_android.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.os.HandlerCompat.postDelayed
import com.example.arvocado_android.R
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.ui.category.CategoryActivity
import com.example.arvocado_android.util.startActivity
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {
    val SPLASH_VIEW_TIME : Long = 2000
    private val authManager : AuthManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed({ //delay를 위한 handler
            if(authManager.autoLogin) {
                startActivity(CategoryActivity::class,true)
            } else {
                startActivity(MainActivity::class,true)
            }
        }, SPLASH_VIEW_TIME)
    }
}