package com.example.arvocado_android.ui.camera

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.arvocado_android.ArVocaDoApplication.Companion.GlobalApp
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.data.request.WordScrapResponse
import com.example.arvocado_android.data.response.CategoryWordResponse
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.util.safeEnqueue
import kotlinx.android.synthetic.main.fragment_learning.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class LearningFragment : Fragment(),fragmentBackPressed {
    private var cameraActivity : CameraActivity? = null
    private lateinit var word : CategoryWordResponse
    private val authManager : AuthManager by inject()
    private val networkManager : NetworkManager by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_learning, container, false)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        cameraActivity = activity as CameraActivity?
    }

    override fun onDetach() {
        super.onDetach()
        cameraActivity = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        word = requireArguments()!!.getSerializable("wordData") as CategoryWordResponse
        Timber.e("word:::${word.w_idx} , ${word.w_kor}")
        val st = word.w_kor+"\n"+word.w_eng
        tvWord.text = st
        if(word.isScraped) {
            Glide.with(imgScrap).load(R.drawable.btn_scrap_active).into(imgScrap)
        } else {
            Glide.with(imgScrap).load(R.drawable.btn_scrap_inactive).into(imgScrap)
        }
        btnBackDown.setOnDebounceClickListener {
            if(authManager.soundCheck) {
                val path: Uri = Uri.parse("android.resource://"+cameraActivity!!.packageName+"/"+R.raw.button_sound)
                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                r3.play()
            }
            cameraActivity!!.backFragment(word.w_idx)
        }
        ivMicStatus.setOnDebounceClickListener {
            cameraActivity!!.endWordFragment(word.w_idx)
        }
        tvWord.setOnDebounceClickListener {
            if(authManager.soundCheck) {
                val path : Uri = Uri.parse(word.audio_eng)
                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                r3.play()
            }
        }
        if(!authManager.token.isNullOrEmpty()) {
            initScrap()
        } else {
            com.example.arvocado_android.util.initWarningDialog(
                GlobalApp,
                str = "로그인 후 이용 가능한 서비스 입니다.",
                str2 = "로그인을 해주세요!"
            )
        }

    }
    private fun initScrap() {
        if(word.isScraped) {
            word.isScraped = !word.isScraped
            Glide.with(imgScrap).load(R.drawable.btn_scrap_inactive).into(imgScrap)

        } else {
            imgScrap.setOnDebounceClickListener {
                networkManager.requestScrap(authManager.token, WordScrapResponse(c_idx = word.c_idx,w_idx = word.w_idx)).safeEnqueue(
                    onSuccess = {
                        if (it.success) {
                            if(authManager.soundCheck) {
                                val path: Uri = Uri.parse("android.resource://"+cameraActivity!!.packageName+"/"+R.raw.button_sound)
                                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                                r3.play()
                            }
                            word.isScraped = !word.isScraped
                            Glide.with(imgScrap).load(R.drawable.btn_scrap_active).into(imgScrap)
                        }
                    },
                    onError = {

                    },
                    onFailure = {

                    }
                )
            }
        }
    }
    override fun onBackPressed() : Boolean {
        return true
    }
}