package com.example.arvocado_android.ui.learning

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.arvocado_android.common.setOnDebounceClickListener
import kotlinx.android.synthetic.main.dialog_guest_learning.*
import com.example.arvocado_android.ui.login.LoginActivity
import com.example.arvocado_android.util.startActivity

class NonLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnCancel.setOnDebounceClickListener {
            Log.d("NONLOGINACTIVITY", "NONLOGINACTIVITY : BTN_CANCEL CLICK LISTENER IS EXECUTED")
            finish()
        }

        btnKeepGoing.setOnDebounceClickListener {
            Log.d("NONLOGINACTIVITY", "NONLOGINACTIVITY : BTN_KEEP_GOING CLICK LISTENER IS EXECUTED")
            onBackPressed()
        }

        btnLogin.setOnDebounceClickListener {
            Log.d("NONLOGINACTIVITY", "NONLOGINACTIVITY : BTN_LOGIN CLICK LISTENER IS EXECUTED")
            startActivity(LoginActivity::class, false)
            finish()
        }

    }
}