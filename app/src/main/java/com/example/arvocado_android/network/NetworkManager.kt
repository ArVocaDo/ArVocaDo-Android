package com.example.arvocado_android.network

import com.example.arvocado_android.data.request.CategoryProgressResponse
import com.example.arvocado_android.data.request.LoginRequest
import com.example.arvocado_android.data.request.SignUpRequest
import com.example.arvocado_android.data.request.WordScrapResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkManager() {

    private val httpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val builder = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(builder)
        .build()
        .create(NetworkService::class.java)

    fun requestLogin(data : LoginRequest) = retrofit.requestLogin(data)
    fun requestSignUp(data : SignUpRequest) = retrofit.requestSignUp(data)
    fun requestSignUpEmail(email : String) = retrofit.requestSignUpEmail(email)
    fun requestCategory(token : String) = retrofit.requestCategory(token)
    fun requestCategoryWord(c_idx : Int) = retrofit.requestCategoryWord(c_idx)
    fun requestCategoryProgress(data : CategoryProgressResponse) = retrofit.requestCategoryProgress(data)
    fun requestScrap(data : WordScrapResponse) = retrofit.requestScrap(data)
    fun requestScrapWord(token: String) = retrofit.requestScarpWord(token)
    fun requestRefreshToken(token: String) = retrofit.requestRefreshToken(token)




    private companion object {
        const val BASE_URL = "http://13.209.121.249"
    }
}

val requestModule = module {
    single { NetworkManager() }
}