package com.example.arvocado_android.ui.learning

import android.content.Context
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
import com.example.arvocado_android.network.AuthManager
import kotlinx.android.synthetic.main.fragment_start.*
import org.koin.android.ext.android.inject

class StartFragment : Fragment() ,fragmentBackPressed{
    private var cameraActivity : LearningActivity? = null
    private lateinit var c_name : String
    private val authManager : AuthManager by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
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

        c_name = requireArguments()!!.getString("c_name").toString()
        tvLearningMain.text= "안녕, 친구야!\n우리 "+c_name+" 단어들을\n공부 해볼까?"

        btnLearningStart.setOnDebounceClickListener {
            if(authManager.soundCheck) {
                startSound()
            }
            cameraActivity!!.finishWordFragment(-1)
        }
        btnBackDown.setOnDebounceClickListener {
            if(authManager.soundCheck) {
                startSound()
            }
            cameraActivity!!.backFragment(0)
        }


    }
    override fun onBackPressed() : Boolean {
        startSound()
        return true
    }
    private fun startSound() {
        val path: Uri =
            Uri.parse("android.resource://" + ArVocaDoApplication!!.GlobalApp.packageName + "/" + R.raw.button_sound)
        val r3: Ringtone = RingtoneManager.getRingtone(ArVocaDoApplication.GlobalApp.applicationContext, path)
        r3.play()
    }
}