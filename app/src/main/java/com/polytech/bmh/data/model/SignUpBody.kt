package com.polytech.bmh.data.model

data class SignUpBody(
    val lastName: String,
    val firstName: String,
    val sex: Boolean,
    val age: Int,
    val email: String,
    val password: String,
    val adress: String,
    val city: String,
    val country: String
)