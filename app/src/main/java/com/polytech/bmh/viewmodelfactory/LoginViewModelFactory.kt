package com.polytech.bmh.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polytech.bmh.repository.LoginRepository
import com.polytech.bmh.viewmodel.LoginViewModel

class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginRepository = LoginRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}