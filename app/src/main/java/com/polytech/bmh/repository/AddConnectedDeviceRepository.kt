package com.polytech.bmh.repository

import com.google.gson.JsonParser
import com.polytech.bmh.data.Result
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceAddBody
import com.polytech.bmh.service.AddConnectedDevice
import com.polytech.bmh.service.RetrofitInstance
import retrofit2.await
import java.lang.Exception

class AddConnectedDeviceRepository {

    suspend fun addConnectedDevice(name: String, description: String, router: String) : Result<ConnectedDeviceAddBody> {

        try {
            val service =
                RetrofitInstance.getRetrofitInstance().create(AddConnectedDevice::class.java)
            val addConnectedDeviceRequest =
                service.addConnectedDevice(ConnectedDeviceAddBody(name, description, router))
            var result = addConnectedDeviceRequest.await()

            val resultAsJsonObject = JsonParser().parse(result.string())

            // if there are no errors
            if (resultAsJsonObject.asJsonObject["message"].isJsonPrimitive) {
                // primitive type. There as no errors.
                return Result.Success(ConnectedDeviceAddBody(name, description, router))
            // if there is an error
            } else {
                // we will look for the error in message.message
                val errorMessage = resultAsJsonObject.asJsonObject["message"].asJsonObject["message"].asString
                return Result.Error(Exception("$errorMessage"))
            }
        }

        catch (e: Exception) {
            return Result.Error(Exception("Erreur lors de l'enregistrement", e))

        }

    }}