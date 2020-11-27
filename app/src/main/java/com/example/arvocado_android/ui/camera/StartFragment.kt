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
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import kotlinx.android.synthetic.main.fragment_start.*
import org.koin.android.ext.android.inject

class StartFragment : Fragment() ,fragmentBackPressed{
    private var cameraActivity : CameraActivity? = null
    private lateinit var c_name : String
    private val authManager : AuthManager by inject()
    private val networkManager : NetworkManager by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
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

        c_name = requireArguments()!!.getString("c_name").toString()
        tvLearningMain.text= "안녕, 친구야!\n우리 "+c_name+" 단어들을\n공부 해볼까?"

        btnLearningStart.setOnDebounceClickListener {
            if(authManager.soundCheck) {
                val path: Uri = Uri.parse("android.resource://"+cameraActivity!!.packageName+"/"+R.raw.button_sound)
                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                r3.play()
            }
            cameraActivity!!.finishWordFragment(0)
        }
        btnBackDown.setOnDebounceClickListener {
            if(authManager.soundCheck) {
                val path: Uri = Uri.parse("android.resource://"+cameraActivity!!.packageName+"/"+R.raw.button_sound)
                val r3: Ringtone = RingtoneManager.getRingtone(context, path)
                r3.play()
            }
            cameraActivity!!.backFragment(1)
        }


    }
    override fun onBackPressed() : Boolean {
        return false
    }
}