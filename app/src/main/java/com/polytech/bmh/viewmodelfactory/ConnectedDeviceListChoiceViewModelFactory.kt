package com.polytech.bmh.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.bmh.data.database.dao.ConnectedDeviceDao
import com.polytech.bmh.repository.ChoiceConnectedDeviceRepository
import com.polytech.bmh.viewmodel.ConnectedDeviceListChoiceViewModel

class ConnectedDeviceListChoiceViewModelFactory(
    private val dataSource: ConnectedDeviceDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConnectedDeviceListChoiceViewModel::class.java)) {
            return ConnectedDeviceListChoiceViewModel(choiceConnectedDeviceRepository = ChoiceConnectedDeviceRepository(), dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}