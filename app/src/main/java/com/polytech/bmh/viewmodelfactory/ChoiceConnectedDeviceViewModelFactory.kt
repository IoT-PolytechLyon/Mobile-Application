package com.polytech.bmh.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.bmh.data.database.dao.ConnectedDeviceDao
import com.polytech.bmh.repository.ChoiceConnectedDeviceRepository
import com.polytech.bmh.viewmodel.ChoiceConnectedDeviceViewModel

class ChoiceConnectedDeviceViewModelFactory(
    private val dataSource: ConnectedDeviceDao,
    private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChoiceConnectedDeviceViewModel::class.java)) {
            return ChoiceConnectedDeviceViewModel(choiceConnectedDeviceRepository = ChoiceConnectedDeviceRepository(), dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}