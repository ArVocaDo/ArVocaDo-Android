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
import com.example.arvocado_android.ArVocaDoApplication
import com.example.arvocado_android.ArVocaDoApplication.Companion.GlobalApp
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.util.initWarningDialog
import kotlinx.android.synthetic.main.activity_signup_email.*
import kotlinx.android.synthetic.main.activity_signup_p_w.*
import kotlinx.android.synthetic.main.dialog_signup_warning.view.*
import timber.log.Timber

class SignupPWActivity : AppCompatActivity() {
    private var pw = ""
    private var email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_p_w)
        email = intent.getStringExtra("email").toString()
        pw = intent.getStringExtra("pw").toString()
        if(!pw.equals("null")) {
            editSignUpP.setText(pw)
        }


        init()
    }
    private fun init() {
        editSignUpP.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                pw = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        imgSignUpPCancle.setOnDebounceClickListener {
            finish()
        }
        imgSignUpPbefore.setOnDebounceClickListener {
               Intent(GlobalApp,SignupEmailActivity::class.java).apply {
                   putExtra("email",email)
               }.run {
                   startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                   finish()
               }

        }
        imgSignUpPNext.setOnDebounceClickListener {
            if(pw == "") {
                initWarningDialog(this,"비밀번호를 입력해주세요.","")
            } else {
                if(pw.length <6) {
                    initWarningDialog(this,"비밀번호를 6자 이상으로 입력해주세요.","")
                } else {
                    Intent(GlobalApp,SignupNicknameActivity::class.java).apply {
                        putExtra("email",email)
                        putExtra("pw",pw)
                    }.run {
                        startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        finish()
                    }
                }

            }

        }
    }
}