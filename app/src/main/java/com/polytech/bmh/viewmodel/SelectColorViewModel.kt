package com.polytech.bmh.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.bmh.data.model.Result
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceData
import com.polytech.bmh.repository.SelectColorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SelectColorViewModel(private val selectColorRepository: SelectColorRepository) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // all information about the connected device selected
    private val _connectedDeviceSelected = MutableLiveData<ConnectedDeviceData>()
    val connectedDeviceSelected: LiveData<ConnectedDeviceData>
        get() = _connectedDeviceSelected

    // response about updating leds color
    private val _updateLedsColorResponse = MutableLiveData<String>()
    val updateLedsColorResponse: LiveData<String>
        get() = _updateLedsColorResponse

    init {
        Log.i("SelectColorViewColor", "created")
    }

    /**
     * Retrieves a specific connected device
     */
    fun getConnectedDeviceById(id: String) {
        uiScope.launch {
            val connectedDeviceByIdResult = selectColorRepository.getConnectedDeviceById(id)

            if (connectedDeviceByIdResult is Result.Success) {
                _connectedDeviceSelected.value = connectedDeviceByIdResult.data
            } else {
                _connectedDeviceSelected.value = null
            }

        }
    }

    /**
     * Updating leds color of a specific connected device
     */
    fun updateConnectedDevice(id: String, redRgb: Int, greenRgb: Int, blueRgb: Int) {
        uiScope.launch {
            val result = selectColorRepository.updateConnectedDevice(id, redRgb, greenRgb, blueRgb)

            if(result is Result.Success) {
                _updateLedsColorResponse.value = "La couleur des leds de l'objet connecté a bien été mise à jour !"
            }
            else {
                _updateLedsColorResponse.value = "Une erreur s'est produite, la mise à jour n'a pas pu se faire !"
            }


        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.i("SelectColorViewColor", "cleared")
    }


}