package com.example.arvocado_android.network

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.koin.dsl.module
import timber.log.Timber
import java.util.*

class AuthManager(context: Context) {

    private val preferences = context.getSharedPreferences(
        AUTH_PREFERENCES,
        Context.MODE_PRIVATE
    )

    var token: String
        get() {
            return preferences.getString(TOKEN_KEY, null).orEmpty()
        }
        set(value) {
            preferences.edit {
                putString(TOKEN_KEY, value)
            }
        }


    var autoLogin: Boolean
        get() {
            return preferences.getBoolean(AUTO_LOGIN_KEY, false)
        }
        set(value) {
            preferences.edit {
                putBoolean(AUTO_LOGIN_KEY, value)
            }
        }

    var soundCheck : Boolean
        get() {
            return preferences.getBoolean(SOUND_KEY, false)
        }
        set(value) {
            preferences.edit{
                putBoolean(SOUND_KEY,value)
            }
        }


    private companion object {
        const val AUTH_PREFERENCES = "auth"
        const val TOKEN_KEY = "token"
        const val AUTO_LOGIN_KEY = "auto"
        const val FIRST_KEY = "first"
        const val SOUND_KEY = "expire"
    }
}

val authModule = module {
    single { AuthManager(get()) }
}