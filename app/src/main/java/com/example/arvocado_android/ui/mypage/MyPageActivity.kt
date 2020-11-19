package com.example.arvocado_android.ui.mypage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.kravelteam.kravel_android.util.startActivity
import kotlinx.android.synthetic.main.activity_my_page.*

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        imgUserScrap.setOnDebounceClickListener {
            startActivity(ScrapWordActivity::class,false)
        }
        imgUserCategory.setOnDebounceClickListener {
            startActivity(ProgressRateActivity::class,false)
        }
    }
}