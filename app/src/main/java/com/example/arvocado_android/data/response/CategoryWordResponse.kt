package com.example.arvocado_android.data.response

import java.io.Serializable

data class CategoryWordResponse(
    val w_idx : Int,
    val c_idx : Int,
    val w_eng : String,
    val w_kor : String,
    val AR_obj : String,
    val AR_mtl : String,
    val AR_sfb: String,
    val audio_eng : String,
    val audio_kor : String,
    val index : Int,
    var isScraped : Boolean
) : Serializable