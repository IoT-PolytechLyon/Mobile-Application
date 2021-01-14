package com.polytech.bmh.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polytech.bmh.data.model.Result
import com.polytech.bmh.data.model.user.signup.SignUpBody
import com.polytech.bmh.data.model.user.signup.SignUpBodyState
import com.polytech.bmh.repository.NewAccountRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NewAccountViewModel(private val newAccountRepository: NewAccountRepository) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _signUpFormState = MutableLiveData<SignUpBodyState>()
    val signUpFormState: LiveData<SignUpBodyState>
        get() = _signUpFormState

    // sign in response
    private val _signUpResponse = MutableLiveData<Result<SignUpBody>>()
    val signUpResponse: LiveData<Result<SignUpBody>>
        get() = _signUpResponse

    init {
        Log.i("NewAccountViewModel", "created")
    }

    /**
     * Sign up function
     */
    fun signUp(lastName: String, firstName: String, sex: String, age: String, email: String, password: String, address: String, city: String, country: String) {

        var genderBoolean = false
        if (sex == "Femme") {
            genderBoolean = true
        }

        val ageInt = age.toInt()

        uiScope.launch {
            val resultSignUp = newAccountRepository.signUp(lastName, firstName, genderBoolean, ageInt, email, password, address, city, country)

            if (resultSignUp is Result.Success) {
                _signUpResponse.value = resultSignUp
            } else {
                _signUpResponse.value = resultSignUp
            }
        }
    }

    /**
     * Function that indicates if the data are in the right format
     */
    fun signUpFormValidate(lastName: String, firstName: String, age: String, email: String, password: String, address: String, city: String) {

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

    /**
     * If the last name is in the right format
     */
    private fun isLastNameValid(lastName: String) : Boolean {
        return lastName.isNotEmpty()
    }

    /**
     * If the first name is in the right format
     */
    private fun isFirstNameValid(firstName: String) : Boolean {
        return firstName.isNotEmpty()
    }

    /**
     * If the age is in the right format
     */
    private fun isAgeValid(age: Int) : Boolean {
        return age in 1..150
    }

    /**
     * If the email is in the right format
     */
    private fun isEmailValid(email: String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * If the password is in the right format
     */
    private fun isPasswordValid(password: String) : Boolean {
        return password.length in 6..32
    }

    /**
     * If the address is in the right format
     */
    private fun isAddressValid(address: String) : Boolean {
        return address.isNotEmpty()
    }

    /**
     * If the city is in the right format
     */
    private fun isCityValid(city: String) : Boolean {
        return city.isNotEmpty()
    }

    /**
     * List of gender for our spinner
     */
    fun listOfGender() : List<String> {
        return arrayOf("Homme", "Femme").toList()
    }

    /**
     * List of countries for our spinner
     */
    fun listOfCountries() : List<String> {
        return arrayOf("France", "Belgique", "Angleterre", "Allemagne", "États-Unis", "Espagne", "Portugal", "Azerbaïdjan").toList()
    }

    /**
     * Converting date of birth to age
     */
    fun dateOfBirthToAge(dateOfBirth: Calendar) : Int {

        var nowDate = Calendar.getInstance()

        var age: Int = nowDate.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR)

        if (nowDate.get(Calendar.DAY_OF_YEAR) < dateOfBirth.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }

    /**
     * Min clickable date on the data picker
     */
    fun minDateVisible(): Calendar {
        var date = Calendar.getInstance()

        var yearMinDate = date.get(Calendar.YEAR) - 150
        date.set(Calendar.YEAR, yearMinDate)

        return date
    }

    /**
     * Max clickable date on the data picker
     */
    fun maxDateVisible() : Calendar {

        var date = Calendar.getInstance()

        var yearMaxDate = date.get(Calendar.YEAR) - 1
        date.set(Calendar.YEAR, yearMaxDate)

        return date
    }

    /**
     * Formatting date
     */
    fun formattedDate(date: Calendar, year: Int, monthOfYear: Int, dayOfMonth: Int) : SimpleDateFormat {
        date.set(Calendar.YEAR, year)
        date.set(Calendar.MONTH, monthOfYear)
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        var myFormat = "dd.MM.yyyy"

        return SimpleDateFormat(myFormat, Locale.FRENCH)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.i("NewAccountViewModel", "cleared")
    }


}