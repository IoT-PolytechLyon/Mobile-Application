package com.polytech.bmh.repository

import com.polytech.bmh.data.Result
import com.polytech.bmh.data.model.user.signup.SignUpBody
import com.polytech.bmh.service.RetrofitInstance
import com.polytech.bmh.service.SignUpUser
import retrofit2.await
import java.io.IOException

class NewAccountRepository {

    suspend fun signUp(lastName: String, firstName: String, sex: Boolean, age: Int, email: String, password: String, address: String, city: String, country: String) : Result<SignUpBody> {
        try {
            val service = RetrofitInstance.getRetrofitInstance().create(SignUpUser::class.java)
            val signUpRequest = service.signUp(SignUpBody(lastName, firstName, sex, age, email, password, address, city, country))
            var result = signUpRequest.await()
            return Result.Success(SignUpBody(lastName, firstName, sex, age, email, password, address, city, country))
        } catch (e: Throwable) {
            return Result.Error(IOException("Erreur lors de la création du compte", e))
        }
    }
}