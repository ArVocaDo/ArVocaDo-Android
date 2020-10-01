package com.kravelteam.kravel_android.util

import com.example.arvocado_android.data.response.BaseResponse
import com.google.gson.Gson
import okhttp3.ResponseBody

fun ResponseBody?.toJson() : BaseResponse<*>
        = Gson().fromJson(this?.string(), BaseResponse::class.java)