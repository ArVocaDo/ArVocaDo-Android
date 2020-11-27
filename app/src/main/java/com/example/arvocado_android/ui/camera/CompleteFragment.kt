package com.example.arvocado_android.ui.camera

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.data.response.CategoryWordResponse
import kotlinx.android.synthetic.main.fragment_complete.*


class CompleteFragment : Fragment(),fragmentBackPressed {
    private var cameraActivity : CameraActivity? = null
    private lateinit var word : CategoryWordResponse
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
        btnCompleteContinue.setOnDebounceClickListener {
            cameraActivity!!.finishWordFragment(word.w_idx)
        }
        btnBackDown.setOnDebounceClickListener {
            cameraActivity!!.backFragment(word.w_idx)
        }

    }

    override fun onBackPressed(): Boolean {
        return true
    }

}