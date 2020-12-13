package com.example.arvocado_android.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.arvocado_android.R
import com.example.arvocado_android.data.response.CategoryWordResponse
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.ui.category.CategoryActivity
import com.example.arvocado_android.util.initLoginWarning
import com.example.arvocado_android.util.safeEnqueue
import com.example.arvocado_android.util.startActivity
import com.example.arvocado_android.util.toast
import kotlinx.android.synthetic.main.activity_camera.*
import org.koin.android.ext.android.inject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    private var c_idx : Int = 0
    private lateinit var c_name : String
    private val networkManager : NetworkManager by inject()
    private val authManager : AuthManager by inject()
    private lateinit var cameraExecutor: ExecutorService
    private var transaction :FragmentTransaction = supportFragmentManager.beginTransaction()
    private lateinit var wordList : List<CategoryWordResponse>

    val list = listOf<Fragment>(StartFragment(), LearningFragment(), CompleteFragment())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        c_idx = intent.getIntExtra("c_idx", 0)
        c_name = intent.getStringExtra("c_name").toString()
        requestCategoryWord()


        initCamera()
        initSettingFragment()



    }
    private fun initSettingFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, StartFragment().apply {
                this.arguments = Bundle().apply {
                    putString("c_name", c_name)
                }
            })
            .commit()

    }
    fun finishWordFragment(w_idx: Int) {
        when(w_idx) {
            list.size -> {
                startActivity(CategoryActivity::class, true)
            }
            else -> {
                requestCategoryWord()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, list[1].apply {
                        this.arguments = Bundle().apply {
                            putSerializable("wordData", wordList[w_idx])
                        }
                    })
                    .commitNow()
            }
        }
    }
    fun endWordFragment(w_idx: Int) {
        requestCategoryWord()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, list[2].apply {
                this.arguments = Bundle().apply {
                    putSerializable("wordData", wordList[w_idx - 1])
                    putInt("totalWord", wordList.size)
                }
            })
            .commitNow()
    }
    fun backFragment(w_idx: Int) {
        when(w_idx) {
            1 -> {
                startActivity(CategoryActivity::class, true)
            }
            else -> {
                supportFragmentManager.beginTransaction().remove(list[1]).commit()

                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, list[1].apply {
                        this.arguments = Bundle().apply {
                            putSerializable("wordData", wordList[w_idx - 1])
                            putInt("totalWord", wordList.size)
                        }
                    }).commit()

            }
        }
    }


    private fun initCamera() {
        if(allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        var cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )
            } catch (e: Exception) {
                Log.e(TAG, "binding falied", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }
    fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "requestCode >> " + requestCode)
        if(requestCode == REQUEST_CODE_PERMISSIONS) {
            if(allPermissionsGranted()) {

                startCamera()
            }
            else {
                Toast.makeText(this, "카메라 접근을 허용해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
    private fun requestCategoryWord() {
            networkManager.requestCategoryWord(authManager.token, c_idx).safeEnqueue(
                onSuccess = {
                    if (it.success) {
                        wordList = it.data
                    } else {
                        authManager.token = "0"
                        authManager.autoLogin = false
                        initLoginWarning(this)
                    }
                },
                onError = {

                },
                onFailure = {

                }
            )

    }
    override fun onBackPressed() {
        //  super.onBackPressed()
        val fragment = this.supportFragmentManager.findFragmentById(R.id.container)

        (fragment as? fragmentBackPressed)?.onBackPressed()?.not()?.let {
            if (it == false) {
                if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                    toast("한번 더 누르면 단어 학습이 종료되고, 카테고리로 넘어가게 됩니다. 종료하시겠습니까?")
                } else {
                    this.finish()
                }
            }
        }
    }
    companion object {
        private const val TAG = "ARVOCADO_CAMERAACTIVITY"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private var backKeyPressedTime : Long = 0
    }
}