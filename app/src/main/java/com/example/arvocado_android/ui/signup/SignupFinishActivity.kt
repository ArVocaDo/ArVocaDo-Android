package com.example.arvocado_android.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.arvocado_android.ArVocaDoApplication.Companion.GlobalApp
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.ui.category.CategoryActivity
import kotlinx.android.synthetic.main.activity_signup_finish.*
import org.koin.android.ext.android.inject

class SignupFinishActivity : AppCompatActivity() {
    private val authManager : AuthManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_finish)

        val token = intent!!.getStringExtra("token")
        imgSignUpF.setOnDebounceClickListener {
                authManager.token = token!!
                authManager.autoLogin = true
                finishAffinity()
               Intent(GlobalApp,CategoryActivity::class.java).run {
                   GlobalApp.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                   finish()
                   overridePendingTransition(0,0)


               }
        }
    }

}