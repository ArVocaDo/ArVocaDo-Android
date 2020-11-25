package com.example.arvocado_android.ui.mypage

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.arvocado_android.R
import com.example.arvocado_android.common.VerticalItemDecorator
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.data.response.CategoryWordResponse
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.ui.adapter.ProgressAdapter
import com.example.arvocado_android.util.networkErrorToast
import com.example.arvocado_android.util.safeEnqueue
import kotlinx.android.synthetic.main.activity_progress_rate.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class ProgressRateActivity : AppCompatActivity() {
    private val progressAdapter : ProgressAdapter by lazy { ProgressAdapter(this) }
    private val networkManager : NetworkManager by inject()
    private val authManager : AuthManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_rate)
        initProgressRecycler()

        imgProgressCancle.setOnDebounceClickListener {
            finish()
        }

    }
    private fun initProgressRecycler() {
        rvProgress.apply {
        adapter = progressAdapter
        addItemDecoration(VerticalItemDecorator(32))
    }

        networkManager.requestCategoryWord(2).safeEnqueue(
            onSuccess = {
                if (it.success) {
                    if (!it.data.isNullOrEmpty()) {
                        progressAdapter.initData(it.data)
                    }
                } else {
                    Timber.e(it.message)
                }
            },
            onFailure = {
            },
            onError = {
                Timber.e(it)
                networkErrorToast()
            }
        )


        progressAdapter.setOnItemClickListener(object : ProgressAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: CategoryWordResponse, pos: Int) {
                if(authManager.soundCheck) {
                    val path: Uri = Uri.parse("android.resource://"+packageName+"/"+R.raw.button_sound)
                    val r3: Ringtone = RingtoneManager.getRingtone(applicationContext, path)
                    r3.play()
                }
            }
        })
    }
}