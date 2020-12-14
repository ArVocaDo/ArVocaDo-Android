package com.example.arvocado_android.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.arvocado_android.ArVocaDoApplication
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.ui.category.CategoryActivity
import com.example.arvocado_android.ui.main.MainActivity
import com.example.arvocado_android.util.startActivity
import kotlinx.android.synthetic.main.activity_my_page.*
import org.koin.android.ext.android.inject

class MyPageActivity : AppCompatActivity() {
    private val networkManager : NetworkManager by inject()
    private val authManager : AuthManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        imgUserScrap.setOnDebounceClickListener {
            startActivity(ScrapWordActivity::class, false)
        }
        imgUserCategory.setOnDebounceClickListener {
            startActivity(ProgressRateActivity::class, false)
        }
        imgUserCancle.setOnDebounceClickListener {
            startActivity(CategoryActivity::class, true)
        }
        imgUserLogout.setOnDebounceClickListener {
            authManager.token="0"
            authManager.autoLogin = false
            Intent(ArVocaDoApplication.GlobalApp, MainActivity::class.java).apply {
            }.run {
                startActivity(this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
                overridePendingTransition(0,0)
            }
        }
    }
}