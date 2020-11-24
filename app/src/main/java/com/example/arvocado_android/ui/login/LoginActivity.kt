package com.example.arvocado_android.ui.login

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.arvocado_android.ArVocaDoApplication
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.data.request.LoginRequest
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.ui.category.CategoryActivity
import com.example.arvocado_android.ui.main.MainActivity
import com.example.arvocado_android.util.initWarningDialog
import com.example.arvocado_android.util.safeEnqueue
import com.example.arvocado_android.util.startActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_signup_warning.view.*
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {
    private val authManager : AuthManager by inject()
    private val networkManager : NetworkManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        imgLoginCancle.setOnDebounceClickListener {
            finish()
        }
        imgLogin.setOnDebounceClickListener {
            requestLogin()
        }
    }
    private fun requestLogin() {
        val email = editLoginEmail.text.toString()
        val pw = editLoginPW.text.toString()

        if(email.isNullOrBlank() || pw.isNullOrBlank()) {
            initWarningDialog(this,"이메일과 비밀번호를 올바르게 입력해주세요!","")
        } else {
            val data = LoginRequest(email,pw)
            networkManager.requestLogin(data).safeEnqueue (
                onSuccess = {
                    if(it.success) {
                        val token = it.data.token
                        authManager.apply {
                            this.token = token
                            autoLogin = true
                        }
                        startActivity(CategoryActivity::class,true)


                    } else {
                        initWarningDialog(this,it.message,"")
                    }
                },
                onError = {

                },
                onFailure = {

                }
            )

        }

    }
}