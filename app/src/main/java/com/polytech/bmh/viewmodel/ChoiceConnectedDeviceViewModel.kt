package com.polytech.bmh.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.bmh.data.Result
import com.polytech.bmh.data.database.dao.ConnectedDeviceDao
import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceProperties
import com.polytech.bmh.repository.ChoiceConnectedDeviceRepository
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.stream.Collector
import java.util.stream.Collectors

class ChoiceConnectedDeviceViewModel(
    private val choiceConnectedDeviceRepository: ChoiceConnectedDeviceRepository,
    private val dataSource: ConnectedDeviceDao) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _connectedDevices = MutableLiveData<List<ConnectedDeviceProperties>>()
    val connectedDevices: LiveData<List<ConnectedDeviceProperties>>
        get() = _connectedDevices

    // connected device selected
    private val _connectedDeviceSelectedId = MutableLiveData<String>()
    val connectedDeviceSelectedId: LiveData<String>
        get() = _connectedDeviceSelectedId

    init {
        Log.i("ListViewModel", "created")
        initializeConnectedDevices()
    }

    private fun initializeConnectedDevices() {
        uiScope.launch {
            _connectedDevices.value = getConnectedDevicesFromDatabase()
        }
    }


    fun getConnectedDevices() {

        uiScope.launch {
            try {
                // gets the connectedDevice from the database.
                // if the database is empty or does not contain everything
                // that is located on the server, we start filling the database.
                val databaseContent = getConnectedDevicesFromDatabase()
                val apiContentResult = choiceConnectedDeviceRepository.getConnectedDevice()
                val apiContent = (apiContentResult as Result.Success).data


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

                val apiIds:List<String> = getIds(apiContent)
                val databaseIds:List<String> = getIds(databaseContent)
                for(databaseConnectedDeviceId in databaseIds) // deletes devices that are not in the api response.
                {
                    if(!apiIds.contains(databaseConnectedDeviceId))
                    {
                        val deviceToDelete:ConnectedDeviceProperties = getById(databaseContent, databaseConnectedDeviceId)
                        delete(deviceToDelete)
                    }
                }


                val result = getConnectedDevicesFromDatabase()
                println(databaseContent[0])
                println(apiContent[0])
                println(result[0])
                _connectedDevices.value = result
            } catch (e: Exception) {
                println(e)
                try {
                    _connectedDevices.value = getConnectedDevicesFromDatabase()
                }
                catch(e2: Exception)
                {
                    println(e2)
                    _connectedDevices.value = null;
                }

            }

        }
    }

    private fun getIds(connectedDevicesTempList:List<ConnectedDeviceProperties>) : List<String>
    {
        val result:MutableList<String> = mutableListOf()
        for(device in connectedDevicesTempList)
        {
            result.add(device._id.toString())
        }
        return result
    }

    private fun getById(connectedDevicesTempList: List<ConnectedDeviceProperties>, id: String) : ConnectedDeviceProperties
    {
        for(device in connectedDevicesTempList)
        {
            if(device._id.toString().equals(id))
            {
                return device
            }
        }
        throw Exception("could not find device in local db")
    }

    /**
     * Retrieve connected device list from the database
     */
    private suspend fun getConnectedDevicesFromDatabase() : List<ConnectedDeviceProperties>
    {
        return withContext(Dispatchers.IO)
        {
            val devices = dataSource.getConnectedDevices()

            devices
        }
    }

    private suspend fun delete(device: ConnectedDeviceProperties)
    {
        withContext(Dispatchers.IO)
        {
            dataSource.delete(device)
        }
    }

    private suspend fun insert(device: ConnectedDeviceProperties)
    {
        withContext(Dispatchers.IO)
        {
            dataSource.insert(device)
        }
    }

    /**
     * When the user clicks on a connected object
     */
    fun onConnectedDeviceClicked(id: String) {
        _connectedDeviceSelectedId.value = id
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}