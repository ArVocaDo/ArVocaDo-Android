package com.example.arvocado_android.ui.signup

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.core.widget.doOnTextChanged
import com.example.arvocado_android.ArVocaDoApplication
import com.example.arvocado_android.ArVocaDoApplication.Companion.GlobalApp
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.util.initWarningDialog
import com.example.arvocado_android.util.safeEnqueue
import kotlinx.android.synthetic.main.activity_signup_email.*
import kotlinx.android.synthetic.main.dialog_signup_warning.view.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class SignupEmailActivity : AppCompatActivity() {
    private var email : String = ""
    private val networkManager : NetworkManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_email)
        email = intent.getStringExtra("email").toString()
        if(!email.equals("null")) {
            editSignUpE.setText(email)
            Timber.e("error + ${email}")
        }


        init()
    }
    private fun init() {
        editSignUpE.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                email = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        imgSignUpE.setOnDebounceClickListener {
            if(email == "") {
                initWarningDialog(this,"이메일을 입력해주세요.","")
            } else {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    initWarningDialog(this,"이메일을 올바르게 입력해주세요.","")
                } else {
                    networkManager.requestSignUpEmail(email).safeEnqueue (
                        onSuccess = {
                            if(it.success) {
                                Intent(GlobalApp,SignupPWActivity::class.java).apply {
                                    putExtra("email",email)
                                }.run {
                                    startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                    finish()
                                }
                            } else {
                                initWarningDialog(this,"이미 회원가입 된 이메일 입니다.","다른 이메일을 입력해주세요.")
                            }
                        },
                        onFailure = {

                        },
                        onError = {

                        }
                    )

                }

            }
        }
        imgSignUpECancle.setOnDebounceClickListener {
            finish()
        }
    }
}