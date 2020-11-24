package com.example.arvocado_android.network

import com.example.arvocado_android.data.request.CategoryProgressResponse
import com.example.arvocado_android.data.request.LoginRequest
import com.example.arvocado_android.data.request.SignUpRequest
import com.example.arvocado_android.data.request.WordScrapResponse
import com.example.arvocado_android.data.response.BaseResponse
import com.example.arvocado_android.data.response.CategoryResponse
import com.example.arvocado_android.data.response.CategoryWordResponse
import com.example.arvocado_android.data.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    /**
     * 로그인
     */
    @POST("/auth/signin")
    fun requestLogin(
        @Body data : LoginRequest
    ): Call<BaseResponse<LoginResponse>>

    /**
     * 회원가입
     */
    @POST("/auth/signup")
    fun requestSignUp(
        @Body data : SignUpRequest
    ) : Call<BaseResponse<LoginResponse>>

    /**
     * 회원가입 - 이메일 중복체크
     */
    @GET("/auth/signup/check")
    fun requestSignUpEmail(
        @Query("email") email:String
    ) : Call<BaseResponse<Unit>>

    /**
     * 카테고리 불러오기
     */
    @GET("/category")
    fun requestCategory(
        @Header("token") token:String
    ) : Call<BaseResponse<List<CategoryResponse>>>

    /**
     * 카테고리 별 단어리스트 가져오기
     */
    @GET("/word")
    fun requestCategoryWord(
        @Query("category") c_idx : Int
    ) : Call<BaseResponse<List<CategoryWordResponse>>>

    /**
     * 단어 학습진도 저장
     */
    @POST("/category/progress")
    fun requestCategoryProgress(
        @Body data : CategoryProgressResponse
    ) : Call<BaseResponse<Unit>>


    /**
     * 단어 스크랩
     */
    @POST("/scrap")
    fun requestScrap(
        @Body data : WordScrapResponse
    ) : Call<BaseResponse<Unit>>


    /**
     * 스크랩 단어 조회
     */
    @GET("/scrap")
    fun requestScarpWord(
        @Header("token") token:String
    ) : Call<BaseResponse<List<CategoryWordResponse>>>





}