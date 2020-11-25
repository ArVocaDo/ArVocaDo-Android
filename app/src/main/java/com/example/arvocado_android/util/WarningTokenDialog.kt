package com.example.arvocado_android.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.arvocado_android.ArVocaDoApplication
import com.example.arvocado_android.R
import com.example.arvocado_android.common.setOnDebounceClickListener
import com.example.arvocado_android.network.AuthManager
import kotlinx.android.synthetic.main.dialog_guest_warning.view.*

fun WarningTokenDialog(context: Context, str : String, str2 : String, authManager: AuthManager) {
    val dialog = androidx.appcompat.app.AlertDialog.Builder(context).create()
    val view = LayoutInflater.from(ArVocaDoApplication.GlobalApp).inflate(R.layout.dialog_guest_warning, null)
    view.clGuestWarning.setBackgroundColor(Color.TRANSPARENT)
    view.txt_area_warning_content1.text = str
    view.txt_area_warning_content2.text = str2
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    view.btnGuestWarning setOnDebounceClickListener {
        dialog.cancel()
        authManager.autoLogin = false
    }

    dialog.apply {
        setView(view)
        setCancelable(false)
        show()
    }
}