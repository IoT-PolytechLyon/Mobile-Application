package com.polytech.bmh.service

import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceAddBody
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceData
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceProperties
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceUpdateBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ConnectedDeviceService {

    @GET("connected-devices")
    fun getConnectedDevices(): Call<List<ConnectedDeviceProperties>>

    @GET("connected-devices/{id}")
    fun getConnectedDevicesById(@Path("id") id: String): Call<ConnectedDeviceData>

    @POST("connected-devices")
    fun addConnectedDevice(@Body info: ConnectedDeviceAddBody): Call<ResponseBody>

    @PUT("connected-devices/{id}")
    fun updateConnectedDevice(@Path("id") id: String, @Body info: ConnectedDeviceUpdateBody): Call<ResponseBody>
}
