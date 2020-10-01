package com.example.arvocado_android

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.example.arvocado_android.network.requestModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class ArVocaDoApplication : Application(), CameraXConfig.Provider{
    override fun onCreate() {
        super.onCreate()

        GlobalApp = this


        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@ArVocaDoApplication)
            modules(
                listOf(
                    requestModule
                )
            )
        }
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }

    companion object {
        lateinit var GlobalApp : ArVocaDoApplication
    }
}