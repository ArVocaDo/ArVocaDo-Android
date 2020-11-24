package com.example.arvocado_android.ui.category

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.arvocado_android.ArVocaDoApplication.Companion.GlobalApp
import com.example.arvocado_android.R
import com.example.arvocado_android.common.GlideApp
import com.example.arvocado_android.common.HorizontalItemDecorator
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.data.response.CategoryResponse
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.ui.adapter.CategoryAdapter
import com.example.arvocado_android.ui.camera.CameraActivity
import com.example.arvocado_android.ui.mypage.MyPageActivity
import com.example.arvocado_android.util.networkErrorToast
import com.example.arvocado_android.util.safeEnqueue
import com.example.arvocado_android.util.startActivity
import com.example.arvocado_android.util.toast
import kotlinx.android.synthetic.main.activity_category.*
import org.koin.android.ext.android.inject
import org.koin.experimental.property.inject
import timber.log.Timber

class CategoryActivity : AppCompatActivity() {
    private val categoryAdatper : CategoryAdapter by lazy { CategoryAdapter() }
    private val networkManager : NetworkManager by inject()
    private val authManager : AuthManager by inject()
    private var audioManager : AudioManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        initCategory()
        initSound()
        imgCategoryUser.setOnDebounceClickListener {
            startActivity(MyPageActivity::class,false)
        }
    }
    private fun initSound() {
        val notificationManager : NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if(!notificationManager.isNotificationPolicyAccessGranted) {
                    toast("소리 권한을 허용해주세요")
                    startActivity(Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
                }

            }

        imgCategorySound.setOnDebounceClickListener {
            if(authManager.soundCheck) {
                authManager.soundCheck = false
                audioManager!!.ringerMode = AudioManager.RINGER_MODE_SILENT
                Glide.with(imgCategorySound).load(R.drawable.btn_sound_off).into(imgCategorySound)

            } else {
                authManager.soundCheck = true
                audioManager!!.ringerMode = AudioManager.RINGER_MODE_NORMAL
                Glide.with(imgCategorySound).load(R.drawable.btn_sound).into(imgCategorySound)
            }
        }
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        when(audioManager!!.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> {
                Glide.with(imgCategorySound).load(R.drawable.btn_sound).into(imgCategorySound)
                authManager.soundCheck = true

            }
            AudioManager.RINGER_MODE_VIBRATE -> {
                Glide.with(imgCategorySound).load(R.drawable.btn_sound_off).into(imgCategorySound)
                authManager.soundCheck = false
            }
            AudioManager.RINGER_MODE_SILENT -> {
                Glide.with(imgCategorySound).load(R.drawable.btn_sound_off).into(imgCategorySound)
                authManager.soundCheck = false
            }
        }
    }
    private fun initCategory() {

        rvCategory.apply {
            adapter = categoryAdatper
            addItemDecoration(HorizontalItemDecorator(14))
        }

        var token = authManager.token
        if(token.isNullOrBlank()) {
            token = "0"
        }
        Timber.e("token $token")

        networkManager.requestCategory(token).safeEnqueue(
            onSuccess = {
                if(it.success) {
                    if (!it.data.isNullOrEmpty()) {
                        categoryAdatper.initData(it.data)
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


        categoryAdatper.setOnItemClickListener(object : CategoryAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: CategoryResponse, pos: Int) {
                if(authManager.soundCheck) {
                    val path: Uri = Uri.parse("android.resource://"+packageName+"/"+R.raw.button_sound)
                    val r3: Ringtone = RingtoneManager.getRingtone(applicationContext, path)
                    r3.play()
                }
                Intent(GlobalApp,CameraActivity::class.java).apply {
                    putExtra("c_name",data.c_name)
                    putExtra("c_idx",data.c_idx)
            }.run {
                GlobalApp.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
            }
        })
    }
}