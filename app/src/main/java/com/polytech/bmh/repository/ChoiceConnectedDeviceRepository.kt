package com.polytech.bmh.repository

import com.polytech.bmh.data.Result
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceProperties
import com.polytech.bmh.service.ConnectedDevicesProperties
import com.polytech.bmh.service.RetrofitInstance
import retrofit2.await
import java.lang.Exception

class ChoiceConnectedDeviceRepository {

    /**
     * Retrieves a list of connected devices
     */
    suspend fun getConnectedDevice() : Result<List<ConnectedDeviceProperties>> {

        try {
            val service = RetrofitInstance.getRetrofitInstance().create(ConnectedDevicesProperties::class.java)
            val connectedDeviceRequest = service.getConnectedDevices()
            val result = connectedDeviceRequest.await()
            return Result.Success(result)
        } catch (e: Throwable) {
            return Result.Error(Exception("Un problème réseau est survenu !", e))
        }

    }


}