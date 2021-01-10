package com.polytech.bmh.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.bmh.data.Result
import com.polytech.bmh.data.model.user.signup.SignUpBody
import com.polytech.bmh.data.model.user.signup.SignUpBodyState
import com.polytech.bmh.repository.NewAccountRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewAccountViewModel(private val newAccountRepository: NewAccountRepository) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _signUpResult = MutableLiveData<Result<SignUpBody>>()
    val signUpResult: LiveData<Result<SignUpBody>>
        get() = _signUpResult

    private val _signUpFormState = MutableLiveData<SignUpBodyState>()
    val signUpFormState: LiveData<SignUpBodyState>
        get() = _signUpFormState

    fun signUp(lastName: String, firstName: String, sex: String, age: String, email: String, password: String, address: String, city: String, country: String) {

        var sexBoolean = false
        if (sex == "Femme") {
            sexBoolean = true
        }

        var ageInt = age.toInt()

        uiScope.launch {
            val result = newAccountRepository.signUp(lastName, firstName, sexBoolean, ageInt, email, password, address, city, country)

            if (result is Result.Success) {
                _signUpResult.value = result
            } else {
                _signUpResult.value = result
            }
        }
    }

    fun signUpFormValidate(lastName: String, firstName: String, age: String, email: String, password: String, address: String, city: String, country: String) {

        var ageInt = 0
        if (age != "") {
             ageInt = age.toInt()
        }

        if(!isLastNameValid(lastName)) {
            _signUpFormState.value = SignUpBodyState(lastNameError = "Le nom est requis !")
        } else if (!isFirstNameValid(firstName)) {
            _signUpFormState.value = SignUpBodyState(firstNameError = "Le prénom est requis !")
        } else if (!isAgeValid(ageInt)) {
            _signUpFormState.value = SignUpBodyState(ageError = "L'âge est invalide : il doit être entre 1 et 150 !")
        } else if (!isEmailValid(email)) {
            _signUpFormState.value = SignUpBodyState(emailError = "L'email est invalide !")
        } else if (!isPasswordValid(password)) {
            _signUpFormState.value = SignUpBodyState(passwordError = "Le mot de passe est invalide : il doit contenir de 6 à 32 caractères !")
        } else if (!isAddressValid(address)) {
            _signUpFormState.value = SignUpBodyState(addressError = "L'adresse est requise !")
        } else if (!isCityValid(city)) {
            _signUpFormState.value = SignUpBodyState(cityError = "Le nom de la ville est requise !")
        } else {
            _signUpFormState.value = SignUpBodyState(isDataValid = true)
        }

    }

    private fun isLastNameValid(lastName: String) : Boolean {
        return lastName.isNotEmpty()
    }

    private fun isFirstNameValid(firstName: String) : Boolean {
        return firstName.isNotEmpty()
    }

    private fun isAgeValid(age: Int) : Boolean {
        return age in 1..150
    }

    private fun isEmailValid(email: String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String) : Boolean {
        return password.length in 6..32
    }

    private fun isAddressValid(address: String) : Boolean {
        return address.isNotEmpty()
    }

    private fun isCityValid(city: String) : Boolean {
        return city.isNotEmpty()
    }

    fun listOfSex() : List<String> {
        return arrayOf("Homme", "Femme").toList()
    }

    fun listOfCountries() : List<String> {
        return arrayOf("France", "Belgique", "Angleterre", "Allemagne", "États-Unis", "Espagne", "Portugal").toList()
    }


}