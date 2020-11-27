package com.example.arvocado_android.ui.camera

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : Fragment() ,fragmentBackPressed{
    private var cameraActivity : CameraActivity? = null
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
        btnLearningStart.setOnDebounceClickListener {
            cameraActivity!!.finishWordFragment(0)
        }
        btnBackDown.setOnDebounceClickListener {
            cameraActivity!!.backFragment(1)
        }


    }
    override fun onBackPressed() : Boolean {
        return false
    }
}