package com.polytech.bmh.data.model.user.signup

data class SignUpBodyState(
    val lastNameError: String? = null,
    val firstNameError: String? = null,
    val ageError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val addressError: String? = null,
    val cityError: String? = null,
    val isDataValid: Boolean = false
)