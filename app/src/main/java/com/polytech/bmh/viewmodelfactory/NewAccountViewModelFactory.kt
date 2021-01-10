package com.polytech.bmh.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.bmh.repository.NewAccountRepository

import com.polytech.bmh.viewmodel.NewAccountViewModel

class NewAccountViewModelFactory : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewAccountViewModel::class.java)) {
            return NewAccountViewModel(newAccountRepository = NewAccountRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}