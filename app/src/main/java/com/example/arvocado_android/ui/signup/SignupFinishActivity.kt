package com.example.arvocado_android.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arvocado_android.ArVocaDoApplication.Companion.GlobalApp
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.ui.category.CategoryActivity
import kotlinx.android.synthetic.main.activity_signup_finish.*

class SignupFinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_finish)

        imgSignUpF.setOnDebounceClickListener {
               Intent(GlobalApp,CategoryActivity::class.java).run {
                   GlobalApp.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                   finish()
               }
        }
    }

}