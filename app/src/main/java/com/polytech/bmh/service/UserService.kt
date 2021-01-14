package com.polytech.bmh.service

import com.polytech.bmh.data.model.user.signin.SignInBody
import com.polytech.bmh.data.model.user.signup.SignUpBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("sign-in")
    fun signIn(@Body info: SignInBody): Call<ResponseBody>

    @POST("sign-up")
    fun signUp(@Body info: SignUpBody): Call<ResponseBody>
}