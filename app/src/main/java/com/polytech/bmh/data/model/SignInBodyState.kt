package com.polytech.bmh.data.model

data class SignInBodyState(
    //val email: String,
    //val password: String
    val emailError: String? = null,
    val passwordError: String? = null,
    val isDataValid: Boolean = false
)
