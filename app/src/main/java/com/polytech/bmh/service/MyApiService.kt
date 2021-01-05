package com.polytech.bmh.service

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.polytech.bmh.data.model.ConnectedDeviceAddBody
import com.polytech.bmh.data.model.SignInBody
import com.polytech.bmh.data.model.SignUpBody
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "http://10.0.2.2:8081"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface ConnectedDevicesProperties {
    @GET("connected-devices")
    fun getConnectedDevices(): Call<List<ConnectedDeviceProperty>>
}

interface SignInUser {
    @POST("sign-in")
    fun signIn(@Body info: SignInBody): Call<ResponseBody>
}

interface SignUpUser {
    @POST("sign-up")
    fun signUp(@Body info: SignUpBody): Call<ResponseBody>
}

interface AddConnectedDevice {
    @POST("connected-devices")
    fun addConnectedDevice(@Body info: ConnectedDeviceAddBody): Call<ResponseBody>
}

object MyApi {
    val retrofitService : ConnectedDevicesProperties by lazy { retrofit.create(ConnectedDevicesProperties::class.java) }
}

class RetrofitInstance {
    companion object {
        val BASE_URL: String = "http://10.0.2.2:8081"

        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }


    }
}

data class ConnectedDeviceProperty(
    val _id: String,
    val name: String,
    val description: String,
    val router: String)

data class ConnectedDevicePropertySubset(
    val _id: String,
    val name: String)