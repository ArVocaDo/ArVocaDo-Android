package com.example.arvocado_android.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.arvocado_android.ArVocaDoApplication
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import kotlinx.android.synthetic.main.dialog_guest_warning.view.*

fun initWarningDialog(context: Context, str : String, str2 : String) {
    val dialog = androidx.appcompat.app.AlertDialog.Builder(context).create()
    val view = LayoutInflater.from(ArVocaDoApplication.GlobalApp).inflate(R.layout.dialog_guest_warning, null)
    view.clGuestWarning.setBackgroundColor(Color.TRANSPARENT)
    view.txt_area_warning_content1.setText(str)
    view.txt_area_warning_content2.setText(str2)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    view.btnGuestWarning setOnDebounceClickListener {
        dialog.cancel()
    }

    dialog.apply {
        setView(view)
        setCancelable(false)
        show()
    }
}