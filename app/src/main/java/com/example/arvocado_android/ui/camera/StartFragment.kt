package com.example.arvocado_android.ui.camera

import android.content.Context
import android.graphics.Camera
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.ui.category.CategoryActivity
import com.example.arvocado_android.ui.main.MainActivity
import com.example.arvocado_android.util.startActivity
import kotlinx.android.synthetic.main.fragment_start.*
import kotlinx.android.synthetic.main.fragment_start.view.*

class StartFragment : Fragment() {
    private lateinit var activity: CameraActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun newInstance(): StartFragment {
        val args = Bundle()
        val fragment = (StartFragment())
        fragment.arguments = args
        return fragment
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = CameraActivity()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView:View = inflater!!.inflate(R.layout.fragment_start,container, false)
        try {
            rootView.btnLearningStart.setOnClickListener { view ->
                Log.d(TAG, "YES!")
            }
        } catch (e : Exception) {
            Log.d(TAG, "NO : " + e)
        }
        if(btnLearningStart != null) {
            btnLearningStart.setOnClickListener {

            }
        }

        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    companion object {
        val TAG = "ARVOCADO_STARTFRAGMENT"
    }
}