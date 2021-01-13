package com.polytech.bmh.repository

import com.google.gson.JsonParser
import com.polytech.bmh.data.model.user.signin.SignInBody
import com.polytech.bmh.service.RetrofitInstance
import com.polytech.bmh.service.SignInUser
import retrofit2.await
import com.polytech.bmh.data.Result
import java.lang.Exception
import java.net.SocketTimeoutException

class LoginRepository {

    /**
     * Function that manages all cases concerning sign in
     */
    suspend fun signIn(email: String, password: String): Result<SignInBody> {

        try {
            val service = RetrofitInstance.getRetrofitInstance().create(SignInUser::class.java)
            val signInRequest = service.signIn(SignInBody(email, password))
            val result = signInRequest.await()
            val resultAsJsonObject = JsonParser().parse(result.string())

            // if the email exists in the database but the password is false
            if (resultAsJsonObject.asJsonObject["message"].asString == "The password is wrong.") {
                return Result.Error(Exception("Le mot de passe est eronné !"))
            }
            // if the email exists in the database but the password is true
            else {
                return Result.Success(SignInBody(email, password))
            }

        } catch (e: Throwable) {
            // if the API does not respond
            if (e.javaClass == SocketTimeoutException::class.java) {
                return Result.Error(
                    Exception(
                        "Un problème réseau est survenu ! Veillez vérifier votre connection Internet !",
                        e
                    )
                )
                // if there is no account with this email
            } else if (e.message == "HTTP 404 Not Found") {
                return Result.Error(
                    Exception(
                        "Il n'existe pas de compte avec cette adresse email. Merci de bien vouloir créer un compte  !",
                        e
                    )
                )
                // if there is an unexpected error
            } else {
                return Result.Error(Exception("Erreur !", e))
            }

        }
    }
}