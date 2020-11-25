
package com.example.arvocado_android.common

import com.example.arvocado_android.network.AuthManager
import com.example.arvocado_android.network.NetworkManager
import com.example.arvocado_android.util.safeEnqueue
import timber.log.Timber

fun newToken(authManager: AuthManager, networkManager: NetworkManager): Boolean{
    var result: Boolean = false
    Timber.e("token :::::: ${authManager.token}")
    if(!authManager.token.isNullOrEmpty()) {
        networkManager.requestRefreshToken(authManager.token).safeEnqueue(
            onSuccess = {
                if (it.message.toString() == "유효한 토큰입니다") {
                    result = true
                    Timber.e(">>>>>>>>>>TRUE")
                }
            },
            onFailure = {
                result = false
            },
            onError = {
                result = false
            }
        )
    } else {
        result = true
    }
    Timber.e("return ??${result}")
    return result
}