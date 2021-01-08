package com.polytech.bmh.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.bmh.repository.SelectColorRepository
import com.polytech.bmh.viewmodel.SelectColorViewModel

class SelectColorViewModelFactory : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelectColorViewModel::class.java)) {
            return SelectColorViewModel(selectColorRepository = SelectColorRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}