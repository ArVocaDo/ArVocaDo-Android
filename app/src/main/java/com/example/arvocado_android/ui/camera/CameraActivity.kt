package com.example.arvocado_android.ui.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arvocado_android.R
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.util.safeEnqueue
import org.koin.android.ext.android.inject
import timber.log.Timber
import timber.log.Timber.e

class CameraActivity : AppCompatActivity() {
    var c_idx : Int = 0
    private val networkManager : NetworkManager by inject()
    private val authManager : AuthManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        c_idx = intent.getIntExtra("c_idx",0)
        requestCategoryWord()
        initCamera()
    }
    private fun initCamera() {

    }
    private fun requestCategoryWord() {
        networkManager.requestCategoryWord(c_idx).safeEnqueue(
            onSuccess = {
                if(it.success) {
                    Timber.e(it.data[0].w_kor)
                }
            },
            onError = {

            },
            onFailure = {

            }
        )
    }
}