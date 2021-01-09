package com.polytech.bmh.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.bmh.data.Result
import com.polytech.bmh.data.model.SignInBodyState
import com.polytech.bmh.data.model.SignInResult
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceData
import com.polytech.bmh.repository.SelectColorRepository
import com.polytech.bmh.ui.login.LoggedInUserView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SelectColorViewModel(private val selectColorRepository: SelectColorRepository) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _connectedDevice = MutableLiveData<ConnectedDeviceData>()
    val connectedDevice: LiveData<ConnectedDeviceData>
        get() = _connectedDevice

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    fun getConnectedDeviceById(id: String) {
        uiScope.launch {
            val result = selectColorRepository.getConnectedDeviceById(id)
            _connectedDevice.value = result
        }
    }

    fun updateConnectedDevice(id: String, redRgb: Int, greenRgb: Int, blueRgb: Int) {
        uiScope.launch {
            val result = selectColorRepository.updateConnectedDevice(id, redRgb, greenRgb, blueRgb)

            if(result is Result.Success) {
                _response.value = "La couleur des led de l'objet connecté n° $id a bien été mis à jour !"
            }
            else {
                _response.value = "Une erreur s'est produite, la mise à jour n'a pas pu se faire !"
            }


        }
    }

}