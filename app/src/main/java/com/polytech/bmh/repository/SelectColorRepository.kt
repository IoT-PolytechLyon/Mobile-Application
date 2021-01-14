package com.polytech.bmh.repository

import com.polytech.bmh.data.model.Result
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceData
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceState
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceStateLed
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceUpdateBody
import com.polytech.bmh.service.ConnectedDeviceService
import com.polytech.bmh.service.RetrofitInstance
import retrofit2.await
import java.io.IOException
import java.lang.Exception

class SelectColorRepository {

    /**
     * Retrieves a connected device by id
     */
    suspend fun getConnectedDeviceById(id: String): Result<ConnectedDeviceData> {
        try {
            val connectedDeviceService = RetrofitInstance.getRetrofitInstance().create(
                ConnectedDeviceService::class.java)
            val getConnectedDeviceByIdRequest = connectedDeviceService.getConnectedDevicesById(id)
            val getConnectedDeviceByIdResult = getConnectedDeviceByIdRequest.await()
            return Result.Success(getConnectedDeviceByIdResult)
        } catch (e: Throwable) {
            return Result.Error(Exception("Une erreur est survenue !", e))
        }

    }

    /**
     * Retrieves led color to a specific connected device
     */
    suspend fun updateConnectedDevice(id: String, redRgb: Int, greenRgb: Int, blueRgb: Int): Result<String> {
        try {
            if (getConnectedDeviceById(id) is Result.Success) {
                val connectedDevice: ConnectedDeviceData = (getConnectedDeviceById(id) as Result.Success).data

                val connectedDeviceLedState = ConnectedDeviceStateLed(connectedDevice.state.led_state.is_on, redRgb, greenRgb, blueRgb
                )

                val connectedDeviceState = ConnectedDeviceState(
                    connectedDevice.state.pir_state,
                    connectedDevice.state.nfc_state, connectedDeviceLedState
                )

                val connectedDeviceService = RetrofitInstance.getRetrofitInstance().create(ConnectedDeviceService::class.java)
                val updateConnectedDeviceRequest = connectedDeviceService.updateConnectedDevice(id, ConnectedDeviceUpdateBody(connectedDeviceState))
                updateConnectedDeviceRequest.await()

                return Result.Success("L'objet connecté a été modifié avec succès !")
            } else {
                return Result.Error(Exception("La mise à jour ne peut pas se faire !"))
            }

        } catch (e: Throwable) {
            return Result.Error(IOException("Erreur lors de la mise à jour !", e))

        }

    }
}