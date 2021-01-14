package com.polytech.bmh.repository

import com.polytech.bmh.data.model.Result
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceProperties
import com.polytech.bmh.service.ConnectedDeviceService
import com.polytech.bmh.service.RetrofitInstance
import retrofit2.await
import java.lang.Exception

class ChoiceConnectedDeviceRepository {

    /**
     * Retrieves a list of connected devices
     */
    suspend fun getConnectedDevice() : Result<List<ConnectedDeviceProperties>> {

        try {
            val connectedDeviceService = RetrofitInstance.getRetrofitInstance().create(ConnectedDeviceService::class.java)
            val connectedDeviceRequest = connectedDeviceService.getConnectedDevices()
            val result = connectedDeviceRequest.await()
            return Result.Success(result)
        } catch (e: Throwable) {
            return Result.Error(Exception("Un problème réseau est survenu !", e))
        }

    }


}