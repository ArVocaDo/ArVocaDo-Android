package com.example.arvocado_android.ui.mypage

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.arvocado_android.R
import com.example.arvocado_android.common.HorizontalItemDecorator
import com.example.arvocado_android.common.VerticalItemDecorator
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.data.response.ScrapWordResponse
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.ui.adapter.ScrapWordAdapter
import com.example.arvocado_android.util.networkErrorToast
import com.example.arvocado_android.util.safeEnqueue
import kotlinx.android.synthetic.main.activity_scrap_word.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class ScrapWordActivity : AppCompatActivity() {
    private val wordAdapter : ScrapWordAdapter by lazy { ScrapWordAdapter(this) }
    private val networkManager : NetworkManager by inject()
    private val authManager : AuthManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrap_word)
        initWordRecycler()
        imgScrapWordCancle.setOnDebounceClickListener {
            finish()
        }
    }
    private fun initWordRecycler () {
        rvScrapWord.apply {
            adapter = wordAdapter
            addItemDecoration(HorizontalItemDecorator(32))
            addItemDecoration(VerticalItemDecorator(32))
        }
        Timber.e("token ::: "+authManager.token)
        networkManager.requestScrapWord(authManager.token).safeEnqueue(
            onSuccess = {
                if (it.success) {
                    if (!it.data.isNullOrEmpty()) {
                        wordAdapter.initData(it.data)
                        tvRvWordTitle.text = it.data.size.toString() + "개"

                    }
                }
            },
            onFailure = {
            },
            onError = {
                Timber.e(it)
                networkErrorToast()
            }
        )


        wordAdapter.setOnItemClickListener(object : ScrapWordAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: ScrapWordResponse, pos: Int) {
                if(authManager.soundCheck) {
                    val path : Uri = Uri.parse(data.audio_eng)
                    val r3: Ringtone = RingtoneManager.getRingtone(this@ScrapWordActivity, path)
                    r3.play()
                }
            }
        })
    }

}