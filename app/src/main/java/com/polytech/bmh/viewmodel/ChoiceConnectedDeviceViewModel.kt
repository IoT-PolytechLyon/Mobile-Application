package com.polytech.bmh.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.bmh.data.model.ConnectedDeviceProperties
import com.polytech.bmh.repository.ChoiceConnectedDeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class ChoiceConnectedDeviceViewModel(private val choiceConnectedDeviceRepository: ChoiceConnectedDeviceRepository) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    private val _connectedDevice = MutableLiveData<List<ConnectedDeviceProperties>>()
    val connectedDevice: LiveData<List<ConnectedDeviceProperties>>
        get() = _connectedDevice

   /*init {
        getConnectedDevices()
    }*/

    fun getConnectedDevices() {

        uiScope.launch {
            try {
                val result = choiceConnectedDeviceRepository.getConnectedDevice()
                _response.value = "Success: ${result.size} connected devices retrieved"
                _connectedDevice.value = result
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
                _connectedDevice.value = null
            }

        }
    }



}