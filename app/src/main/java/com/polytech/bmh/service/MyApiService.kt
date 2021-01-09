package com.polytech.bmh.service

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.polytech.bmh.data.model.*
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceData
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceState
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

//private const val BASE_URL = "http://10.0.2.2:8081"
//private const val BASE_URL = "http://192.168.0.55:8081"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/*private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()*/

interface ConnectedDevicesProperties {
    @GET("connected-devices")
    fun getConnectedDevices(): Call<List<ConnectedDeviceProperties>>

    @GET("connected-devices/{id}")
    fun getConnectedDevicesById(@Path("id") id: String): Call<ConnectedDeviceData>
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

interface UpdateConnectedDevice {
    @PUT("connected-devices/{id}")
    fun updateConnectedDevice(@Path("id") id: String, @Body info: ConnectedDeviceData): Call<ResponseBody>
}


/*object MyApi {
    val retrofitService : ConnectedDevicesProperties by lazy { retrofit.create(ConnectedDevicesProperties::class.java) }
}
*/
class RetrofitInstance {
    companion object {
        //val BASE_URL: String = "http://192.168.0.55:8081"
        val BASE_URL: String = "http://10.0.2.2:8081"

        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }


    }
}
