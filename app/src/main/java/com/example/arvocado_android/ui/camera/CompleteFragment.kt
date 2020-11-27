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
import com.example.arvocado_android.ArVocaDoApplication
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.data.response.CategoryWordResponse
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.ui.main.MainActivity
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
            if (!authManager.token.isNullOrEmpty()) {
                cameraActivity!!.finishWordFragment(word.w_idx)
                if(authManager.soundCheck) {
                    val path: Uri = Uri.parse("android.resource://"+cameraActivity!!.packageName+"/"+R.raw.button_sound)
                    val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                    r3.play()
                }
//                networkManager.requestCategoryProgress(authManager.token, CategoryProgressResponse(word.c_idx, word.w_idx)).safeEnqueue(
//                    onSuccess = {
//                        if(it.success) {
//
//                        }
//                    },
//                    onFailure = {
//
//                    },
//                    onError = {
//
//                    }
//                )

            } else {
                initWarningDialog()
            }
        }
        btnBackDown.setOnDebounceClickListener {
            cameraActivity!!.backFragment(word.w_idx)
            if(authManager.soundCheck) {
                val path: Uri = Uri.parse("android.resource://"+cameraActivity!!.packageName+"/"+R.raw.button_sound)
                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                r3.play()
            }
        }

    }
    private fun initWarningDialog() {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireActivity()).create()
        val view = LayoutInflater.from(ArVocaDoApplication.GlobalApp).inflate(R.layout.dialog_guest_learning, null)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view.btnCancel.setOnDebounceClickListener {
            if(authManager.soundCheck) {
                val path: Uri = Uri.parse("android.resource://"+cameraActivity!!.packageName+"/"+R.raw.button_sound)
                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                r3.play()
            }
            dialog.cancel()
        }
        view.btnLogin.setOnDebounceClickListener {
            if(authManager.soundCheck) {
                val path: Uri = Uri.parse("android.resource://"+cameraActivity!!.packageName+"/"+R.raw.button_sound)
                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                r3.play()
            }
            startActivity(MainActivity::class, true)
        }
        view.btnKeepGoing.setOnDebounceClickListener {
            if(authManager.soundCheck) {
                val path: Uri = Uri.parse("android.resource://"+cameraActivity!!.packageName+"/"+R.raw.button_sound)
                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                r3.play()
            }
            cameraActivity!!.finishWordFragment(word.w_idx)
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