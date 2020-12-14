package com.example.arvocado_android.ui.learning

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.arvocado_android.ArVocaDoApplication
import com.example.arvocado_android.R
import com.example.arvocado_android.common.GlideApp
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.data.request.WordScrapResponse
import com.example.arvocado_android.data.response.CategoryWordResponse
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.ui.sound.PapagoTextTranslate
import com.example.arvocado_android.util.initWarningDialog
import com.example.arvocado_android.util.safeEnqueue
import com.example.arvocado_android.util.setInvisible
import com.example.arvocado_android.util.toast
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager
import com.kakao.util.helper.Utility
import kotlinx.android.synthetic.main.fragment_learning.*
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LearningFragment : Fragment(),fragmentBackPressed {
    private var cameraActivity: LearningActivity? = null
    private lateinit var word: CategoryWordResponse
    private val authManager: AuthManager by inject()
    private val networkManager: NetworkManager by inject()
    private var hashKey: String? = null
    private var checkClick: Boolean = false
    private var checkLearn: Boolean = false
    private lateinit var builder: SpeechRecognizerClient.Builder
    private lateinit var client: SpeechRecognizerClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //SDK 초기화
        hashKey = getKeyHash(requireContext())
        SpeechRecognizerManager.getInstance().initializeLibrary(requireContext())

        //클라이언트 생성
        builder = SpeechRecognizerClient
            .Builder()
            .setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WORD)

        client = builder.build()
        return inflater.inflate(R.layout.fragment_learning, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cameraActivity = activity as LearningActivity?
    }

    override fun onDetach() {
        super.onDetach()
        cameraActivity = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        word = requireArguments()!!.getSerializable("wordData") as CategoryWordResponse
        Timber.e("word:${word.w_kor}")
        Timber.e("word:::${word.w_idx} , ${word.w_kor}")
        val st = word.w_kor+"\n"+word.w_eng
        tvWord.text = st
        if(word.isScraped) {
            Glide.with(imgScrap).load(R.drawable.btn_scrap_active).into(imgScrap)
        } else {
            Glide.with(imgScrap).load(R.drawable.btn_scrap_inactive).into(imgScrap)
        }

        Glide.with(ivMicStatus).load(R.drawable.ic_mic).into(ivMicStatus)
        checkClick= false


        btnBackDown.setOnDebounceClickListener {
            if (authManager.soundCheck) {
                startSound()
            }
            cameraActivity!!.backFragment(word.index)
        }
        ivMicStatus.setOnDebounceClickListener {
            if (authManager.soundCheck) {
                startSound()
            }
            if (checkClick) {
                Glide.with(ivMicStatus).load(word.w_img).into(ivMicStatus)

            } else {
                if (checkLearn) {
                    client.cancelRecording()
                    checkLearn = false
                    tvLearningMain.text = "보이는 단어를 마이크를 눌러 읽어볼까?"
                    Glide.with(ivMicStatus).load(R.drawable.ic_mic).into(ivMicStatus)
                    ivMicSound.setInvisible()
                    checkClick = false

                } else {
                    setupPermissions()
                }
            }
        }
        imgComplete.setOnDebounceClickListener {
            cameraActivity!!.endWordFragment(word.index)
        }
        initScrap()

        tvWord.setOnDebounceClickListener {
            if (authManager.soundCheck) {
                val path: Uri = Uri.parse(word.audio_eng)
                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                r3.play()
            }
        }

    }

    private fun setupPermissions() {
        var permission_audio = ContextCompat.checkSelfPermission(
            cameraActivity!!.applicationContext,
            Manifest.permission.RECORD_AUDIO
        )
        var permission_storage = ContextCompat.checkSelfPermission(
            cameraActivity!!.applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission_audio != PackageManager.PERMISSION_GRANTED) {
            Timber.e("퍼미션 거절")
            initWarningDialog(requireContext(), "음성 권한을 허용해주세요!", "")
            tvLearningMain.text = "음성 권한을 허용해야 단어 학습이 가능해요!"
            Glide.with(ivMicStatus).load(R.drawable.ic_mic_fail).into(ivMicStatus)
            ActivityCompat.requestPermissions(
                cameraActivity!!,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_REQUEST_CODE
            )
        } else if (permission_storage != PackageManager.PERMISSION_GRANTED) {
            Timber.e("퍼미션 거절")
            initWarningDialog(requireContext(), "음성 권한을 허용해주세요!", "")
            tvLearningMain.text = "음성 권한을 허용해야 단어 학습이 가능해요!"
            Glide.with(ivMicStatus).load(R.drawable.ic_mic_fail).into(ivMicStatus)
            ActivityCompat.requestPermissions(
                cameraActivity!!,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_REQUEST_CODE
            )
        } else {
            //본문실행
            tvLearningMain.text = "보이는 단어를 마이크를 눌러 읽어볼까?"
            Glide.with(ivMicStatus).load(R.drawable.ic_mic).into(ivMicStatus)
            ivMicSound.setInvisible()
            startUsingSpeechSDK()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    initWarningDialog(requireContext(), "음성 권한을 허용해주세요!", "")

                } else {
//                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                }
            }

            STORAGE_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    initWarningDialog(requireContext(), "음성 권한을 허용해주세요!", "")

                } else {
//                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startUsingSpeechSDK() {
        toast("음성인식 시작!")
        //Callback
        client.setSpeechRecognizeListener(object : SpeechRecognizeListener {
            //콜백함수들
            override fun onReady() {
                Timber.d("모든 하드웨어 및 오디오 서비스가 준비되었습니다.")
                val msg = micHandler.obtainMessage()
                micHandler.sendMessage(msg)
                checkLearn = true

            }

            override fun onBeginningOfSpeech() {
                Timber.d("사용자가 말을 하기 시작했습니다.")
            }

            override fun onEndOfSpeech() {
                Timber.d("사용자의 말하기가 끝이 났습니다. 데이터를 서버로 전달합니다.")

            }

            override fun onPartialResult(partialResult: String?) {
                //현재 인식된 음성테이터 문자열을 출력해 준다. 여러번 호출됨. 필요에 따라 사용하면 됨.
                //Log.d(TAG, "현재까지 인식된 문자열:" + partialResult)
                Timber.d("현재 까지 인식된 문자열:" + partialResult)


            }

            /*
            최종결과 - 음성입력이 종료 혹은 stopRecording()이 호출되고 서버에 질의가 완료되고 나서 호출됨
            Bundle에 ArrayList로 값을 받음. 신뢰도가 높음 것 부터...
             */
            override fun onResults(results: Bundle?) {
                val texts =
                    results?.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS)
                val confs =
                    results?.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES)

                //정확도가 높은 첫번째 결과값을 텍스트뷰에 출력
                cameraActivity!!.runOnUiThread {
                    val word = texts?.get(0).toString()
                    val clientId = resources.getString(R.string.naver_client_id)
                    val clientKey = resources.getString(R.string.naver_client_secret)
                    object : Thread() {
                        override fun run() {
                            val tranMode = PapagoTextTranslate()
                            val result: String
                            result = tranMode.getTranslation(clientId, clientKey, word, "ko", "en")
                            val resultBundle = Bundle()
                            resultBundle.putString("resultWord", result)
                            val msg = transper_handler.obtainMessage()
                            msg.data = resultBundle
                            transper_handler.sendMessage(msg)
                        }
                    }.start()
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

    @SuppressLint("HandlerLeak")
    var micHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            tvLearningMain.text = "이제 단어를 읽어줘!"
            Glide.with(ivMicStatus).load(R.drawable.btn_vui_bg).into(ivMicStatus)
            Glide.with(ivMicSound).load(R.drawable.sound).into(ivMicSound)
            ivMicSound.visibility = View.VISIBLE
        }
    }

    @SuppressLint("HandlerLeak")
    var transper_handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val bundle = msg.data
            val resultText = bundle.getString("resultWord")!!.toLowerCase()
            Timber.e("resultText!!!!!!!!!!!!!!!!!!!!!!!!${resultText}")
            if (resultText.equals(word.w_eng)) {
                checkClick = true
                tvLearningMain.text = "딩동댕 정답입니다!!"
                Glide.with(ivMicStatus).load(word.w_img).into(ivMicStatus)
                imgComplete.visibility = View.VISIBLE
                ivMicStatus.visibility = View.INVISIBLE
                ivMicSound.visibility = View.INVISIBLE
                imgWord.visibility = View.VISIBLE
                /**
                 * 정답시 나오는화면
                 */
                GlideApp.with(imgWord).load(word.w_img).into(imgWord)
                tvExplain.visibility = View.VISIBLE
                imgWord.setOnClickListener {
                    if (authManager.soundCheck) {
                        startSound()
                    }
                    Intent(ArVocaDoApplication.GlobalApp, ArcoreActivity::class.java).apply {
                        putExtra("w_AR",word.w_AR)
                        putExtra("w_kor",word.w_kor)
                        putExtra("w_eng",word.w_eng)
                        putExtra("audio_eng",word.audio_eng)
                    }.run {
                        startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    }


                }
            } else {
                checkClick = false
                tvLearningMain.text = "땡 ! 다시 한번 도전해볼까?"
                Glide.with(ivMicStatus).load(R.drawable.ic_mic_incorrect).into(ivMicStatus)
                ivMicSound.visibility = View.INVISIBLE
                imgComplete.visibility = View.INVISIBLE

            }
        }
    }


    private fun getKeyHash(context: Context): String? {
        val packageInfo =
            Utility.getPackageInfo(context, PackageManager.GET_SIGNATURES) ?: return null

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

    private fun initScrap() {
        imgScrap.setOnDebounceClickListener {
            if (authManager.token!="0") {
                networkManager.requestScrap(
                    authManager.token,
                    WordScrapResponse(c_idx = word.c_idx, w_idx = word.w_idx)
                ).safeEnqueue(
                    onSuccess = {
                        if (it.success) {
                            if (authManager.soundCheck) {
                                startSound()
                            }
                            word.isScraped = !word.isScraped
                            if (word.isScraped) {
                                Glide.with(imgScrap).load(R.drawable.btn_scrap_active)
                                    .into(imgScrap)

                            } else {

                                Glide.with(imgScrap).load(R.drawable.btn_scrap_inactive)
                                    .into(imgScrap)
                            }

                        }
                    },
                    onError = {

                    },
                    onFailure = {

                    }
                )
            } else {
                initWarningDialog(
                    requireContext(),
                    str = "로그인 후 이용 가능한 서비스 입니다.",
                    str2 = "로그인을 해주세요!"
                )

            }
        }
    }
    override fun onBackPressed(): Boolean {
        startSound()
        return true
    }
    private fun startSound() {
        val path: Uri =
            Uri.parse("android.resource://" + ArVocaDoApplication!!.GlobalApp.packageName + "/" + R.raw.button_sound)
        val r3: Ringtone = RingtoneManager.getRingtone(ArVocaDoApplication.GlobalApp.applicationContext, path)
        r3.play()
    }
    companion object {
        private val RECORD_REQUEST_CODE = 1001
        private val STORAGE_REQUEST_CODE = 1002
    }



}