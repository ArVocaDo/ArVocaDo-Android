package com.example.arvocado_android.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.arvocado_android.ArVocaDoApplication
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.ui.main.MainActivity
import kotlinx.android.synthetic.main.dialog_guest_warning.view.*

fun initLoginWarning(context: Context) {
    val dialog = androidx.appcompat.app.AlertDialog.Builder(context).create()
    val view = LayoutInflater.from(ArVocaDoApplication.GlobalApp).inflate(R.layout.dialog_guest_warning, null)
    view.clGuestWarning.setBackgroundColor(Color.TRANSPARENT)
    view.txt_area_warning_content1.setText("로그인을 다시 해주세요!")
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    view.btnGuestWarning setOnDebounceClickListener {
        Intent(ArVocaDoApplication.GlobalApp, MainActivity::class.java).run {
            ArVocaDoApplication.GlobalApp.startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    dialog.apply {
        setView(view)
        setCancelable(false)
        show()
    }
}