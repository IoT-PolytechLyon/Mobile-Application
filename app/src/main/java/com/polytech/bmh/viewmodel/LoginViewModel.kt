package com.polytech.bmh.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.bmh.data.Result
import com.polytech.bmh.data.model.SignInBodyState
import com.polytech.bmh.data.model.SignInResult
import com.polytech.bmh.repository.LoginRepository
import com.polytech.bmh.data.model.user.signin.LoggedInUserView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val signInFormState = MutableLiveData<SignInBodyState>()
    val signInFormBodyState: LiveData<SignInBodyState>
        get() = signInFormState

    private val loginResponse = MutableLiveData<SignInResult>()
    val loginResponseBody: LiveData<SignInResult>
        get() = loginResponse


    fun signIn(email: String, password: String) {

            uiScope.launch {
                val result = loginRepository.signIn(email, password)
                if (result is Result.Success) {
                    loginResponse.value =
                        SignInResult(success = LoggedInUserView(displayName = "${result.data.email}"))
                } else {
                    loginResponse.value = SignInResult(error = "Erreur lors de la connexion")
                }
            }


    }

    private fun isEmailValid(email: String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String) : Boolean {
        return password.length in 6..32
    }

    fun signInFormValidate(email: String, password: String) {

        if(!isEmailValid(email)) {
            signInFormState.value = SignInBodyState(emailError = "L'email est invalide !")
        } else if (!isPasswordValid(password)) {
            signInFormState.value = SignInBodyState(passwordError = "Le mot de passe est invalide : il doit contenir de 6 à 32 caractères !")
        } else {
            signInFormState.value = SignInBodyState(isDataValid = true)
        }

    }

}