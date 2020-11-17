package com.example.arvocado_android.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arvocado_android.ArVocaDoApplication.Companion.GlobalApp
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.ui.category.CategoryActivity
import com.example.arvocado_android.ui.login.LoginActivity
import com.example.arvocado_android.ui.mypage.MyPageActivity
import com.example.arvocado_android.ui.signup.SignupEmailActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {
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
            Intent(GlobalApp,CategoryActivity::class.java).run {
                GlobalApp.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }

    }
}