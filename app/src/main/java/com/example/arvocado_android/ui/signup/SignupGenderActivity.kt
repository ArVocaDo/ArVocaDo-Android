package com.example.arvocado_android.ui.signup

import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.arvocado_android.ArVocaDoApplication
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.data.request.SignUpRequest
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.util.initWarningDialog
import com.example.arvocado_android.util.safeEnqueue
import kotlinx.android.synthetic.main.activity_signup_gender.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class SignupGenderActivity : AppCompatActivity() {
    private var email = ""
    private var pw = ""
    private var gender = ""
    private var nickname = ""

    private val networkManager : NetworkManager by inject()
    private val authManager : AuthManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_gender)
        email = intent.getStringExtra("email").toString()
        pw = intent.getStringExtra("pw").toString()
        nickname = intent.getStringExtra("nickname").toString()

        init()
    }
    private fun init() {
        rgSignUpGender.setOnCheckedChangeListener(object :RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, checkedId: Int) {
                if(checkedId == R.id.btnSignUpGBoy) {
                    gender = "M"
                }
                if(checkedId == R.id.btnSignUpGGirl) {
                    gender = "F"
                }
            }

        })
        imgSignUpGCancle.setOnDebounceClickListener {
            finish()
        }
        imgSignUpGFinish.setOnDebounceClickListener {
            if(gender!="") {
                Timber.e("아이디 : "+email)
                Timber.e("비밀번호 : "+pw)
                Timber.e("성별 : "+gender)
                Timber.e("닉네임 : "+nickname)
                requestSignUp()

            } else {
                initWarningDialog(this,"성별을 선택해주세요.","")
            }
        }
        imgSignUpGbefore.setOnDebounceClickListener {
            Intent(ArVocaDoApplication.GlobalApp,SignupNicknameActivity::class.java).apply {
                putExtra("email",email)
                putExtra("pw",pw)
                putExtra("nickname",nickname)
            }.run {
                startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                finish()
            }
        }
    }
    private fun requestSignUp(){
        val data = SignUpRequest(email,pw,nickname,gender)
        networkManager.requestSignUp(data).safeEnqueue(
            onSuccess = {
                if(it.success) {
                    Intent(ArVocaDoApplication.GlobalApp,SignupFinishActivity::class.java).apply {
                        putExtra("token",it.data.token)
                    }.run {
                        startActivity(this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }
                }
            },
            onFailure = {

            },
            onError = {

            }
        )
    }
}