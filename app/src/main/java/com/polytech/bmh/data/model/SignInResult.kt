package com.polytech.bmh.data.model

import com.polytech.bmh.ui.login.LoggedInUserView

data class SignInResult (
    val success: LoggedInUserView? = null,
    val error: String? = null
)