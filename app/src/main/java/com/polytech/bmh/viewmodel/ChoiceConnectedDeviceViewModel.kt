package com.polytech.bmh.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.bmh.data.database.dao.ConnectedDeviceDao
import com.polytech.bmh.data.model.ConnectedDeviceProperties
import com.polytech.bmh.repository.ChoiceConnectedDeviceRepository
import kotlinx.coroutines.*
import java.lang.Exception

class ChoiceConnectedDeviceViewModel(
    private val choiceConnectedDeviceRepository: ChoiceConnectedDeviceRepository,
    private val dataSource: ConnectedDeviceDao) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    private val _connectedDevice = MutableLiveData<List<ConnectedDeviceProperties>>()
    val connectedDevice: LiveData<List<ConnectedDeviceProperties>>
        get() = _connectedDevice

    private val _connectedDeviceSelected = MutableLiveData<ConnectedDeviceProperties>()
    val connectedDeviceSelected: LiveData<ConnectedDeviceProperties>
        get() = _connectedDeviceSelected

    private val _connectedDeviceListUi = MutableLiveData<List<String>>()
    val connectedDeviceListUi: LiveData<List<String>>
        get() = _connectedDeviceListUi

    fun getConnectedDevices() {

        uiScope.launch {
            try {
                // gets the connectedDevice from the database.
                // if the database is empty or does not contain everything
                // that is located on the server, we start filling the database.
                val databaseContent = getConnectedDevicesFromDatabase()
                val apiContent = choiceConnectedDeviceRepository.getConnectedDevice()

                for(connectedDevice in apiContent) // fills database content if different
                {
                    var doNotAdd = false
                    for(databaseDevice in databaseContent)
                    {
                        if(databaseDevice._id == connectedDevice._id)
                        {
                            doNotAdd = true
                        }
                    }
                    if(!doNotAdd)
                    {
                        insert(connectedDevice)
                    }
                }
                val result = getConnectedDevicesFromDatabase()
                println(databaseContent[0])
                println(apiContent[0])
                println(result[0])
                _connectedDevice.value = result
            } catch (e: Exception) {
                println(e)
                _connectedDevice.value = null
            }

        }
    }

    private suspend fun getConnectedDevicesFromDatabase() : List<ConnectedDeviceProperties>
    {
        return withContext(Dispatchers.IO)
        {
            val devices = dataSource.getConnectedDevices()

            devices
        }
    }

    private suspend fun insert(device:ConnectedDeviceProperties)
    {
        withContext(Dispatchers.IO)
        {
            dataSource.insert(device)
        }
    }

    fun getListConnectedDevicesUI(connectedDeviceList :List<ConnectedDeviceProperties>) {
        val listConnectedDevicePropertySubset : MutableList<String> = mutableListOf()
        for (connectedDevice in connectedDeviceList) {
            val connectedDeviceUi = "${connectedDevice.name} : ${connectedDevice.router}"
            listConnectedDevicePropertySubset.add(connectedDeviceUi)
        }
        _connectedDeviceListUi.value = listConnectedDevicePropertySubset
    }

    fun getValue(connectedDeviceSelected: ConnectedDeviceProperties) {
        _connectedDeviceSelected.value = connectedDeviceSelected

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}