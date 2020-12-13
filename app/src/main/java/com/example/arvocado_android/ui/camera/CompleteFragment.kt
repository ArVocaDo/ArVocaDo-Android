package com.example.arvocado_android.ui.camera

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.arvocado_android.ArVocaDoApplication.Companion.GlobalApp
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.data.request.CategoryProgressResponse
import com.example.arvocado_android.data.response.CategoryWordResponse
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.ui.main.MainActivity
import com.example.arvocado_android.util.initLoginWarning
import com.example.arvocado_android.util.safeEnqueue
import com.example.arvocado_android.util.startActivity
import kotlinx.android.synthetic.main.dialog_guest_learning.view.*
import kotlinx.android.synthetic.main.fragment_complete.*
import org.koin.android.ext.android.inject
import kotlin.math.roundToInt


class CompleteFragment : Fragment(),fragmentBackPressed {
    private var cameraActivity : CameraActivity? = null
    private lateinit var word : CategoryWordResponse
    private val networkManager : NetworkManager by inject()
    private val authManager : AuthManager by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete, container, false)
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
        var total = requireArguments()!!.getInt("totalWord")

        var p =  (((word.w_idx / total.toDouble()) * 100).toDouble())
        tvProgressPercent.text = p.roundToInt().toString()+ "%"
        pbComplete.setProgress(p.roundToInt())
        pbComplete.max = 100f
        pbComplete.setSecondaryProgress((p*1.3).roundToInt())
        btnCompleteContinue.setOnDebounceClickListener {
            if (!authManager.token.equals("0")) {
                networkManager.requestCategoryProgress(authManager.token, CategoryProgressResponse(word.c_idx, word.index)).safeEnqueue(
                    onSuccess = {
                        if(it.success) {
                            cameraActivity!!.finishWordFragment(word.w_idx)
                            if(authManager.soundCheck) {
                                val path: Uri = Uri.parse("android.resource://"+ mp3packageName!!+"/"+R.raw.button_sound)
                                val r3: Ringtone = RingtoneManager.getRingtone(GlobalApp.applicationContext, path)
                                r3.play()
                            }

                        } else {
                            authManager.token = "0"
                            authManager.autoLogin = false
                            initLoginWarning(cameraActivity!!)
                        }
                    },
                    onFailure = {

                    },
                    onError = {

                    }
                )

            } else {
                initGuestWarning()
            }
        }
        btnBackDown.setOnDebounceClickListener {
            cameraActivity!!.backFragment(word.index)
            if(authManager.soundCheck) {
                val path: Uri = Uri.parse("android.resource://"+ GlobalApp!!.packageName+"/"+R.raw.button_sound)
                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                r3.play()
            }
        }

    }
    private fun initGuestWarning() {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireActivity()).create()
        val view = LayoutInflater.from(GlobalApp).inflate(R.layout.dialog_guest_learning, null)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view.btnCancel.setOnClickListener {
            if(authManager.soundCheck) {
                val path: Uri = Uri.parse("android.resource://"+GlobalApp!!.packageName+"/"+R.raw.button_sound)
                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                r3.play()
            }
            dialog.cancel()
        }
        view.btnLogin.setOnClickListener {
            if(authManager.soundCheck) {
                val path: Uri = Uri.parse("android.resource://"+GlobalApp!!.packageName+"/"+R.raw.button_sound)
                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                r3.play()
            }
            startActivity(MainActivity::class, true)
            dialog.cancel()
        }
        view.btnKeepGoing.setOnClickListener {
            if(authManager.soundCheck) {
                val path: Uri = Uri.parse("android.resource://"+requireActivity()!!.packageName+"/"+R.raw.button_sound)
                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                r3.play()
            }
            cameraActivity!!.finishWordFragment(word.w_idx)
            dialog.cancel()
        }

        dialog.apply {
            setView(view)
            setCancelable(false)
            show()
        }
    }
    override fun onBackPressed(): Boolean {
        return true
    }


}