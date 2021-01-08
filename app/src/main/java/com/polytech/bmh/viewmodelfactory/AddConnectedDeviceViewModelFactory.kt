package com.polytech.bmh.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.bmh.repository.AddConnectedDeviceRepository
import com.polytech.bmh.repository.ChoiceConnectedDeviceRepository
import com.polytech.bmh.viewmodel.AddConnectedDeviceViewModel
import com.polytech.bmh.viewmodel.ChoiceConnectedDeviceViewModel

class AddConnectedDeviceViewModelFactory: ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddConnectedDeviceViewModel::class.java)) {
            return AddConnectedDeviceViewModel(addConnectedDeviceRepository = AddConnectedDeviceRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}