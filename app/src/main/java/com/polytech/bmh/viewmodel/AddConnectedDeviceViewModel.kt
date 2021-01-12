package com.polytech.bmh.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.bmh.repository.AddConnectedDeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import com.polytech.bmh.data.Result
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceAddBodyState
import com.polytech.bmh.data.model.DataResult

class AddConnectedDeviceViewModel(private val addConnectedDeviceRepository: AddConnectedDeviceRepository) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // status of field values
    private val _addConnectedDeviceBodyState = MutableLiveData<ConnectedDeviceAddBodyState>()
    val addConnectedDeviceBodyState: LiveData<ConnectedDeviceAddBodyState>
        get() = _addConnectedDeviceBodyState

    // add a new connected device response
    private val _addNewConnectedDeviceResponse = MutableLiveData<DataResult>()
    val addNewConnectedDeviceResponse: LiveData<DataResult>
        get() = _addNewConnectedDeviceResponse

    /**
     * Add a new connected device
     */
    fun addConnectedDevice(name: String, description: String, router: String) {

        uiScope.launch {
            val result = addConnectedDeviceRepository.addConnectedDevice(name, description, router)
            // if there are no errors
            if (result is Result.Success) {
                _addNewConnectedDeviceResponse.value = DataResult(success = "L'ajout de l'objet connecté a été effectué avec succès !")
             // if there is a error
            } else {
                _addNewConnectedDeviceResponse.value = DataResult(error = (result as Result.Error).exception.message)
            }
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
    fun addConnectedDeviceFormValidate(name: String, description: String, router: String) {

        if(!isConnectedDeviceNameValid(name)) {
            _addConnectedDeviceBodyState.value = ConnectedDeviceAddBodyState(nameError = "Le nom de l'objet connecté est invalide ! (il ne doit pas être vide)")
        } else if(!isConnectedDeviceDescriptionValid(description)) {
            _addConnectedDeviceBodyState.value = ConnectedDeviceAddBodyState(descriptionError = "La description de l'objet connecté est invalide ! (elle ne doit pas être vide)")
        } else if(!isConnectedDeviceRouterValid(router)) {
            _addConnectedDeviceBodyState.value = ConnectedDeviceAddBodyState(routerError = "Le router de l'objet connecté est invalide ! (il doit être de la forme d'une adresse IP)")
        } else {
            _addConnectedDeviceBodyState.value = ConnectedDeviceAddBodyState(isDataValid = true)
        }


    }
}