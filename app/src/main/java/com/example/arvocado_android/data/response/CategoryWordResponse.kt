package com.example.arvocado_android.data.response

import java.io.Serializable

data class CategoryWordResponse(
    val w_idx : Int,
    val c_idx : Int,
    val w_eng : String,
    val w_kor : String,
    val w_img : String,
    val audio_eng : String,
    val audio_kor : String,
    val index : Int,
    var isScraped : Boolean,
    val w_AR : String
) : Serializable