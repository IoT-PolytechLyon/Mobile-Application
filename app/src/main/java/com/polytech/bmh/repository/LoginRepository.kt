package com.polytech.bmh.repository

import com.polytech.bmh.data.model.SignInBody
import com.polytech.bmh.service.RetrofitInstance
import com.polytech.bmh.service.SignInUser
import retrofit2.await
import java.io.IOException
import com.polytech.bmh.data.Result

class LoginRepository {

    suspend fun signIn(email: String, password: String) : Result<SignInBody> {

        try {
            val service = RetrofitInstance.getRetrofitInstance().create(SignInUser::class.java)
            val signInRequest = service.signIn(SignInBody(email, password))
            var result = signInRequest.await()
            return Result.Success(SignInBody(email, password))
        } catch(e: Throwable) {
            return Result.Error(IOException("Erreur lors de la connexion", e))
        }
    }
}