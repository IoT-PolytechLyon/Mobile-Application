package com.polytech.bmh.repository

import com.polytech.bmh.data.Result
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceData
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceState
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceStateLed
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceUpdateBody
import com.polytech.bmh.service.ConnectedDevicesProperties
import com.polytech.bmh.service.RetrofitInstance
import com.polytech.bmh.service.UpdateConnectedDevice
import retrofit2.await
import java.io.IOException

class SelectColorRepository {

    suspend fun getConnectedDeviceById(id: String) : ConnectedDeviceData {
            val service = RetrofitInstance.getRetrofitInstance().create(ConnectedDevicesProperties::class.java)
            val getConnectedDeviceByIdRequest = service.getConnectedDevicesById(id)
            var result = getConnectedDeviceByIdRequest.await()
            return result
    }

    suspend fun updateConnectedDevice(id: String, redRgb: Int, greenRgb: Int, blueRgb: Int) : Result<String> {
        try {
            val connectedDevice: ConnectedDeviceData = getConnectedDeviceById(id)

            val connectedDeviceLedState = ConnectedDeviceStateLed(connectedDevice.state.led_state.is_on,
                redRgb, greenRgb, blueRgb)

            val connectedDeviceState = ConnectedDeviceState(connectedDevice.state.pir_state,
                connectedDevice.state.nfc_state, connectedDeviceLedState)

            val service = RetrofitInstance.getRetrofitInstance().create(UpdateConnectedDevice::class.java)
            val updateConnectedDeviceRequest = service.updateConnectedDevice(id, ConnectedDeviceUpdateBody(connectedDeviceState))
            val result = updateConnectedDeviceRequest.await()
            return Result.Success("L'objet connecté a été modifié avec succès !")
        } catch (e: Throwable) {
            return Result.Error(IOException("Erreur lors de la mise à jour !", e))

        }



    }
}