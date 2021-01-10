package com.polytech.bmh.data.model

import com.polytech.bmh.data.model.user.signin.LoggedInUserView

data class SignInResult (
    val success: LoggedInUserView? = null,
    val error: String? = null
)