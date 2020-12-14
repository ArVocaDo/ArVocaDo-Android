package com.example.arvocado_android.ui.mypage

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.arvocado_android.ArVocaDoApplication
import com.example.arvocado_android.R
import com.example.arvocado_android.common.VerticalItemDecorator
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.data.response.CategoryResponse
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.ui.adapter.ProgressAdapter
import com.example.arvocado_android.ui.camera.CameraActivity
import com.example.arvocado_android.util.initLoginWarning
import com.example.arvocado_android.util.networkErrorToast
import com.example.arvocado_android.util.safeEnqueue
import kotlinx.android.synthetic.main.activity_progress_rate.*
import kotlinx.android.synthetic.main.dialog_start_warning.view.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class ProgressRateActivity : AppCompatActivity() {
    private val progressAdapter : ProgressAdapter by lazy { ProgressAdapter(this) }
    private val networkManager : NetworkManager by inject()
    private val authManager : AuthManager by inject()
    private var index : Int = 0
    private var c_idx : Int = 0
    private var c_name : String =""
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

            networkManager.requestCategory(authManager.token).safeEnqueue(
                onSuccess = {
                    if (it.success) {
                        if (!it.data.isNullOrEmpty()) {
                            progressAdapter.initData(it.data)
                        }
                    } else {
                        authManager.token = "0"
                        authManager.autoLogin = false
                        initLoginWarning(this)
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
            override fun onItemClick(v: View, data: CategoryResponse, pos: Int) {
                if(authManager.soundCheck) {
                    startSound()
                }
                c_idx = data.c_idx
                c_name = data.c_name
                index = data.index
                startSound()
                if(data.c_count == index) {
                    initWarningStartDlg()
                } else {
                    Intent(ArVocaDoApplication.GlobalApp, CameraActivity::class.java).apply {
                        putExtra("c_idx",c_idx)
                        putExtra("c_name",c_name)
                        putExtra("index",index-1)
                        putExtra("mode","PROGRESS")
                    }.run {
                        startActivity(this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }
                }

            }
        })
    }
    private fun initWarningStartDlg() {
        val dialog = AlertDialog.Builder(applicationContext).create()
        val view = LayoutInflater.from(applicationContext).inflate(R.layout.dialog_guest_warning, null)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view.btnKeepGoing.setOnDebounceClickListener {
            if(authManager.soundCheck) {
                startSound()
            }
            Intent(ArVocaDoApplication.GlobalApp, CameraActivity::class.java).apply {
                putExtra("c_idx",c_idx)
                putExtra("c_name",c_name)
                putExtra("index",0)
                putExtra("mode","PROGRESS")
            }.run {
                startActivity(this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
            dialog.cancel()

        }
        view.btnCancel.setOnDebounceClickListener {
            if(authManager.soundCheck) {
                startSound()
            }
            dialog.cancel()
        }

        dialog.apply {
            setView(view)
            setCancelable(false)
            show()
        }}

    private fun startSound() {
        val path: Uri = Uri.parse("android.resource://"+packageName+"/"+R.raw.button_sound)
        val r3: Ringtone = RingtoneManager.getRingtone(applicationContext, path)
        r3.play()
    }
}