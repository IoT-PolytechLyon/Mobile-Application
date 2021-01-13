package com.polytech.bmh.viewmodel

import android.nfc.FormatException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.bmh.data.Result
import com.polytech.bmh.data.model.DataResult
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceAddBodyState
import com.polytech.bmh.repository.AddConnectedDeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

    init {
        Log.i("AddCDViewModel", "created")
    }

    /**
     * Add a new connected device
     */
    fun addConnectedDevice(name: String, description: String, router: String) {

        uiScope.launch {
            val result = addConnectedDeviceRepository.addConnectedDevice(name, description, router)
            // if there are no errors
            if (result is Result.Success) {
                _addNewConnectedDeviceResponse.value =
                    DataResult(success = "L'ajout de l'objet connecté a été effectué avec succès !")
                // if there is a error
            } else {
                if ((result as Result.Error).exception.javaClass != FormatException::class.java) {
                    _addNewConnectedDeviceResponse.value =
                        DataResult(error = result.exception.message)
                }
            }

            val validateConnectedDevice =
                addConnectedDeviceRepository.validateConnectedDeviceBody(name, description, router)
            _addConnectedDeviceBodyState.value = validateConnectedDevice
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.i("AddCDViewModel", "cleared")
    }

}
