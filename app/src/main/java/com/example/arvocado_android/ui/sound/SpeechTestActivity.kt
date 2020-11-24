package com.example.arvocado_android.ui.sound

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.*
import android.speech.SpeechRecognizer
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.arvocado_android.R
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager
import com.kakao.util.helper.Utility.getPackageInfo
import com.kravelteam.kravel_android.util.toast
import kotlinx.android.synthetic.main.activity_speech_test.*
import timber.log.Timber
import java.lang.ref.WeakReference
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SpeechTestActivity() : AppCompatActivity() {
    private var hashKey : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_test)

        hashKey = getKeyHash(applicationContext)
        bt_search.setOnClickListener {
            toast("음성 녹음 시작")
            setupPermissions()
            tv_result.setText("")
        }
    }

    fun getKeyHash(context: Context): String? {
        val packageInfo = getPackageInfo(context,PackageManager.GET_SIGNATURES) ?: return null

        for (signature in packageInfo!!.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d(ContentValues.TAG, Base64.encodeToString(md.digest(), Base64.NO_WRAP))
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
            } catch (e: NoSuchAlgorithmException) {
                Log.w(ContentValues.TAG, "Unable to get MessageDigest. signature=$signature", e)
            }

        }
        return null
    }

    private fun setupPermissions(){
        var permission_audio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        var permission_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(permission_audio != PackageManager.PERMISSION_GRANTED) {
            Timber.e("퍼미션 거절")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_REQUEST_CODE)
        } else if(permission_storage != PackageManager.PERMISSION_GRANTED) {
            Timber.e("퍼미션 거절")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)
        } else {
            //본문실행
            startUsingSpeechSDK()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                }
            }

            STORAGE_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun startUsingSpeechSDK(){
        Toast.makeText(this, "Start Newton", Toast.LENGTH_SHORT).show()

        //SDK 초기화
        SpeechRecognizerManager.getInstance().initializeLibrary(this)

        //버튼 클릭



            //클라이언트 생성
            val builder = SpeechRecognizerClient.Builder().setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WEB)
            val client = builder.build()

            //Callback
            client.setSpeechRecognizeListener(object : SpeechRecognizeListener {
                //콜백함수들
                override fun onReady() {
                    Timber.d("모든 하드웨어 및 오디오 서비스가 준비되었습니다.")
//                    toast("모든 하드웨어 및 오디오 서비스가 준비되었습니다.")
                }

                override fun onBeginningOfSpeech() {
                    Timber.d("사용자가 말을 하기 시작했습니다.")
//                    toast("사용자가 말을 하기 시작했습니다.")
                }

                override fun onEndOfSpeech() {
                    Timber.d("사용자의 말하기가 끝이 났습니다. 데이터를 서버로 전달합니다.")
//                    toast("사용자의 말하기가 끝이 났습니다. 데이터를 서버로 전달합니다.")

                }

                override fun onPartialResult(partialResult: String?) {
                    //현재 인식된 음성테이터 문자열을 출력해 준다. 여러번 호출됨. 필요에 따라 사용하면 됨.
                    //Log.d(TAG, "현재까지 인식된 문자열:" + partialResult)
                }

                /*
                최종결과 - 음성입력이 종료 혹은 stopRecording()이 호출되고 서버에 질의가 완료되고 나서 호출됨
                Bundle에 ArrayList로 값을 받음. 신뢰도가 높음 것 부터...
                 */
                override fun onResults(results: Bundle?) {
                    val texts = results?.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS)
                    val confs = results?.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES)

                    //정확도가 높은 첫번째 결과값을 텍스트뷰에 출력
                    runOnUiThread {
                        tv_result.setText(texts?.get(0))
                        toast(texts?.get(0).toString())
                    }


                }

                override fun onAudioLevel(audioLevel: Float) {
                    //Log.d(TAG, "Audio Level(0~1): " + audioLevel.toString())
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                    //에러 출력 해 봄
                    Timber.e("error :${errorMsg}")
                }
                override fun onFinished() {
                }
            })

            //음성인식 시작함
            client.startRecording(true)

    }

    companion object {
        private val RECORD_REQUEST_CODE = 1001
        private val STORAGE_REQUEST_CODE = 1002
    }

}