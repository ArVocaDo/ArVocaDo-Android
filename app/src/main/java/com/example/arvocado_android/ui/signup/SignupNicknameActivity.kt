package com.example.arvocado_android.ui.signup

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import com.example.arvocado_android.ArVocaDoApplication
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import kotlinx.android.synthetic.main.activity_signup_nickname.*
import kotlinx.android.synthetic.main.activity_signup_p_w.*
import kotlinx.android.synthetic.main.dialog_signup_warning.view.*

class SignupNicknameActivity : AppCompatActivity() {
    private var pw = ""
    private var email = ""
    private var nickname = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_nickname)

        email = intent.getStringExtra("email").toString()
        pw = intent.getStringExtra("pw").toString()
        nickname = intent.getStringExtra("nickname").toString()
        if(!nickname.equals("null")) {
            editSignUpN.setText(nickname)
        }

        init()
    }
    private fun init() {
        editSignUpN.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                nickname = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        imgSignUpNCancle.setOnDebounceClickListener {
            finish()
        }
        imgSignUpNBefore.setOnDebounceClickListener {
            Intent(ArVocaDoApplication.GlobalApp,SignupPWActivity::class.java).apply {
                putExtra("email",email)
                putExtra("pw",pw)
            }.run {
                startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                finish()
            }

        }
        imgSignUpNNext.setOnDebounceClickListener {
            if(nickname == "") {
                initWarningDialog("닉네임을 입력해주세요.")
            } else {
                Intent(ArVocaDoApplication.GlobalApp,SignupGenderActivity::class.java).apply {
                    putExtra("email",email)
                    putExtra("pw",pw)
                    putExtra("nickname",nickname)
                }.run {
                    startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    finish()
                }


            }

        }
    }

    private fun initWarningDialog(str : String) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this).create()
        val view = LayoutInflater.from(ArVocaDoApplication.GlobalApp).inflate(R.layout.dialog_signup_warning, null)
        view.clWarningBg.setBackgroundColor(Color.TRANSPARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view.tvWarningOK.setOnDebounceClickListener {
            dialog.cancel()
        }
        view.tvWarningTitle.text = str

        dialog.apply {
            setView(view)
            setCancelable(false)
            show()
        }
    }
}