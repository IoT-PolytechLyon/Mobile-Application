package com.polytech.bmh.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.bmh.repository.ChoiceConnectedDeviceRepository
import com.polytech.bmh.viewmodel.ChoiceConnectedDeviceViewModel

class ChoiceConnectedDeviceViewModelFactory : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChoiceConnectedDeviceViewModel::class.java)) {
            return ChoiceConnectedDeviceViewModel(choiceConnectedDeviceRepository = ChoiceConnectedDeviceRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}