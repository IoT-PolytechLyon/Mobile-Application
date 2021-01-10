package com.polytech.bmh.data.model

data class SignInBodyState(
    val emailError: String? = null,
    val passwordError: String? = null,
    val isDataValid: Boolean = false
)
