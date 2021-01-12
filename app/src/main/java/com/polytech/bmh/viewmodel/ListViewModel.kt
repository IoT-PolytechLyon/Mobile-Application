package com.polytech.bmh.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polytech.bmh.data.database.dao.ConnectedDeviceDao
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceProperties
import kotlinx.coroutines.*

class ListViewModel(
    val database: ConnectedDeviceDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _connectedDevices = MutableLiveData<List<ConnectedDeviceProperties>>()
    val connectedDevices: LiveData<List<ConnectedDeviceProperties>>
        get() = _connectedDevices
    private val _connectedDeviceDetail = MutableLiveData<String>()
    val connectedDeviceDetail: LiveData<String>
        get() = _connectedDeviceDetail

    init {
        Log.i("ListViewModel", "created")
        initializeConnectedDevices()
    }

    private fun initializeConnectedDevices() {
        uiScope.launch {
            _connectedDevices.value = getConnectedDevicesFromDatabase()
        }
    }

    private suspend fun getConnectedDevicesFromDatabase(): List<ConnectedDeviceProperties>? {
        return withContext(Dispatchers.IO) {

            var connectedDevices = database.getConnectedDevices()
            connectedDevices
        }
    }

    fun onConnectedDeviceClicked(id: String) {
        _connectedDeviceDetail.value = id
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("ListViewModel", "destroyed")
        viewModelJob.cancel()
    }
}