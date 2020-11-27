package com.example.arvocado_android.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.arvocado_android.ArVocaDoApplication.Companion.GlobalApp
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.ui.category.CategoryActivity
import com.example.arvocado_android.ui.login.LoginActivity
import com.example.arvocado_android.ui.signup.SignupEmailActivity
import com.example.arvocado_android.util.startActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val authManager : AuthManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    private fun init() {
        imgSignUp.setOnDebounceClickListener {
            Intent(GlobalApp,SignupEmailActivity::class.java).run {
                GlobalApp.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
        imgLogin.setOnDebounceClickListener {
            Intent(GlobalApp,LoginActivity::class.java).run {
                GlobalApp.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
        tvGuestMode.setOnDebounceClickListener {
            authManager.token = "0"
            authManager.autoLogin = false
           startActivity(CategoryActivity::class, true)
        }

    }
}