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
import com.polytech.bmh.data.model.ConnectedDeviceAddBody
import com.polytech.bmh.data.model.ConnectedDeviceAddBodyState
import com.polytech.bmh.data.model.DataResult

class AddConnectedDeviceViewModel(private val addConnectedDeviceRepository: AddConnectedDeviceRepository) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _addConnectedDeviceBody = MutableLiveData<ConnectedDeviceAddBody>()
    val addConnectedDeviceBody: LiveData<ConnectedDeviceAddBody>
        get() = _addConnectedDeviceBody

    private val _response = MutableLiveData<DataResult>()
    val response: LiveData<DataResult>
        get() = _response

    private val _addConnectedDeviceBodyState = MutableLiveData<ConnectedDeviceAddBodyState>()
    val addConnectedDeviceBodyState: LiveData<ConnectedDeviceAddBodyState>
        get() = _addConnectedDeviceBodyState


    fun addConnectedDevice(name: String, description: String, router: String) {

        uiScope.launch {
            val result = addConnectedDeviceRepository.addConnectedDevice(name, description, router)
            if (result is Result) {
                _addConnectedDeviceBody.value = ConnectedDeviceAddBody(name, description, router)
                _response.value = DataResult(success = result.toString())
            } else {
                _addConnectedDeviceBody.value = null
                _response.value = DataResult(error = result.toString())
            }
        }


    }

    private fun isNameValid(name: String): Boolean {
        return name.isNotEmpty()
    }

    private fun isDescriptionValid(description: String): Boolean {
        return description.isNotEmpty()
    }

    private fun isRouterValid(router: String): Boolean {
        return Patterns.IP_ADDRESS.matcher(router).matches()
    }

    fun addConnectedDeviceFormChanged(name: String, description: String, router: String) {

        if(!isNameValid(name)) {
            _addConnectedDeviceBodyState.value = ConnectedDeviceAddBodyState(nameError = "Le nom de l'objet connecté est invalide ! (il ne doit pas être vide)")
        } else if(!isDescriptionValid(description)) {
            _addConnectedDeviceBodyState.value = ConnectedDeviceAddBodyState(descriptionError = "La description de l'objet connecté est invalide ! (elle ne doit pas être vide)")
        } else if(!isRouterValid(router)) {
            _addConnectedDeviceBodyState.value = ConnectedDeviceAddBodyState(routerError = "Le router de l'objet connecté est invalide ! (il doit être de la forme d'une adresse IP)")
        } else {
            _addConnectedDeviceBodyState.value = ConnectedDeviceAddBodyState(isDataValid = true)
        }


    }
}