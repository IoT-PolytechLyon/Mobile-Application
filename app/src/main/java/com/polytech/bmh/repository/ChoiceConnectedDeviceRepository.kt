package com.polytech.bmh.repository

import com.polytech.bmh.data.model.ConnectedDeviceProperties
import com.polytech.bmh.service.ConnectedDevicesProperties
import com.polytech.bmh.service.RetrofitInstance
import retrofit2.await

class ChoiceConnectedDeviceRepository {

    suspend fun getConnectedDevice() : List<ConnectedDeviceProperties> {

        val service = RetrofitInstance.getRetrofitInstance().create(ConnectedDevicesProperties::class.java)
        val connectedDeviceRequest = service.getConnectedDevices()
        var result = connectedDeviceRequest.await()

        return result
    }
}