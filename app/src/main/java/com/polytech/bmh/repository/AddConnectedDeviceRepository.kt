package com.polytech.bmh.repository

import android.nfc.FormatException
import android.util.Patterns
import com.google.gson.JsonParser
import com.polytech.bmh.data.model.Result
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceAddBody
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceAddBodyState
import com.polytech.bmh.service.ConnectedDeviceService
import com.polytech.bmh.service.RetrofitInstance
import retrofit2.await
import java.lang.Exception

class AddConnectedDeviceRepository {

    suspend fun addConnectedDevice(
        name: String,
        description: String,
        router: String
    ): Result<ConnectedDeviceAddBody> {

        try {
            val connectedDeviceService = RetrofitInstance.getRetrofitInstance().create(
                ConnectedDeviceService::class.java)
            val addConnectedDeviceRequest =
                connectedDeviceService.addConnectedDevice(ConnectedDeviceAddBody(name, description, router))
            val result = addConnectedDeviceRequest.await()

            // checks if a field is not valid
            val validateConnectedDevice = validateConnectedDeviceBody(name, description, router)
            val resultAsJsonObject = JsonParser().parse(result.string())

            if (validateConnectedDevice.isDataValid) {
                return Result.Success(ConnectedDeviceAddBody(name, description, router))
            }
            else {
                // we will look for the error in message.message
                val errorMessage =
                    resultAsJsonObject.asJsonObject["message"].asJsonObject["message"].asString
                return Result.Error(FormatException(errorMessage))
            }

        } catch (e: Exception) {
            return Result.Error(
                Exception(
                    "Un problème réseau est survenu ! Veillez vérifier votre connection Internet !\"",
                    e
                )
            )

        }

    }

    /**
     * If the connected device name is in the right format
     */
    private fun isConnectedDeviceNameValid(name: String): Boolean {
        return name.isNotEmpty()
    }

    /**
     * If the connected device description is in the right format
     */
    private fun isConnectedDeviceDescriptionValid(description: String): Boolean {
        return description.isNotEmpty()
    }

    /**
     * If the connected device router is in the right format
     */
    private fun isConnectedDeviceRouterValid(router: String): Boolean {
        return Patterns.IP_ADDRESS.matcher(router).matches()
    }

    /**
     * Function that indicates if the data are in the right format
     */
    fun validateConnectedDeviceBody(name: String, description: String, router: String) : ConnectedDeviceAddBodyState {
        if(!isConnectedDeviceNameValid(name)) {
            return ConnectedDeviceAddBodyState(nameError = "Le nom de l'objet connecté est invalide ! (il ne doit pas être vide)")
        } else if(!isConnectedDeviceDescriptionValid(description)) {
            return ConnectedDeviceAddBodyState(descriptionError = "La description de l'objet connecté est invalide ! (elle ne doit pas être vide)")
        } else if(!isConnectedDeviceRouterValid(router)) {
            return ConnectedDeviceAddBodyState(routerError = "Le router de l'objet connecté est invalide ! (il doit être de la forme d'une adresse IP)")
        } else {
            return ConnectedDeviceAddBodyState(isDataValid = true)
        }


    }
}