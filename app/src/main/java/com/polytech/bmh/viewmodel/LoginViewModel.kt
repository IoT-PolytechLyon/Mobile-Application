package com.polytech.bmh.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.bmh.data.Result
import com.polytech.bmh.data.model.DataResult
import com.polytech.bmh.data.model.user.signin.SignInBodyState
import com.polytech.bmh.repository.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // status of field values
    private val _signInFormState = MutableLiveData<SignInBodyState>()
    val signInFormState: LiveData<SignInBodyState>
        get() = _signInFormState

    // sign in response
    private val _signInResponse = MutableLiveData<DataResult>()
    val signInResponse: LiveData<DataResult>
        get() = _signInResponse

    init {
        Log.i("LoginViewModel", "created")
    }

    /**
     * Login function
     */
    fun signIn(email: String, password: String) {

        uiScope.launch {
            val result = loginRepository.signIn(email, password)
            if (result is Result.Success) {
                _signInResponse.value = DataResult(success = "Bienvenue $email !")
            } else {
                _signInResponse.value = DataResult(error = (result as Result.Error).exception.message)
            }
        }

    }

    /**
     * If the email is in the right format
     */
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * If the password is in the right format
     */
    private fun isPasswordValid(password: String): Boolean {
        return password.length in 6..32
    }

    /**
     * Function that indicates if the data are in the right format
     */
    fun signInFormValidate(email: String, password: String) {

        if (!isEmailValid(email)) {
            _signInFormState.value = SignInBodyState(emailError = "L'email est invalide !")
        } else if (!isPasswordValid(password)) {
            _signInFormState.value =
                SignInBodyState(passwordError = "Le mot de passe est invalide : il doit contenir de 6 à 32 caractères !")
        } else {
            _signInFormState.value = SignInBodyState(isDataValid = true)
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.i("LoginViewModel", "cleared")
    }

}