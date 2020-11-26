package com.example.arvocado_android.ui.camera

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.example.arvocado_android.R
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.util.safeEnqueue
import kotlinx.android.synthetic.main.activity_camera.*
import okhttp3.internal.EMPTY_REQUEST
import org.koin.android.ext.android.inject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    var c_idx : Int = 0
    private val networkManager : NetworkManager by inject()
    private val authManager : AuthManager by inject()
    private lateinit var cameraExecutor: ExecutorService
    private val firstFragment: BlankFragment = BlankFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        c_idx = intent.getIntExtra("c_idx",0)
        requestCategoryWord()
        initCamera()
        val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, firstFragment)
        transaction.commit()
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
          Log.d(TAG, "ARVOCADO: startCamera()")
//        val cameraProviderFuture1 = ProcessCameraProvider.getInstance(this)
//        Log.d(TAG, "ARVOCADO: 1")
//        cameraProviderFuture1.addListener(Runnable {
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture1.get()
//            Log.d(TAG, "ARVOCADO: 2")
//
//            val preview = Preview.Builder()
//                .build()
//                .also {
//                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
//                }
//            Log.d(TAG, "ARVOCADO: 3")
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//            try {
//                cameraProvider.unbindAll()
//
//                cameraProvider.bindToLifecycle(
//                    this, cameraSelector, preview)
//                Log.d(TAG, "ARVOCADO: 4")
//            } catch(e: Exception) {
//                Log.d(TAG, "ARVOCADO: 5")
//                Log.e(TAG, "binding falied", e)
//            }
//        }, ContextCompat.getMainExecutor(this))
//        Log.d(TAG, "ARVOCADO: 6")
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "requestCode >> "+requestCode)
        if(requestCode == REQUEST_CODE_PERMISSIONS) {
            if(allPermissionsGranted()) {
                Log.d(TAG, "ARVOCADO: 8")
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

            networkManager.requestCategoryWord(c_idx).safeEnqueue(
                onSuccess = {
                    if (it.success) {
                    }
                },
                onError = {

                },
                onFailure = {

                }
            )

    }
    companion object {
        private const val TAG = "ARVOCADO_CAMERAACTIVITY"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}