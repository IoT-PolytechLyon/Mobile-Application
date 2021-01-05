package com.polytech.bmh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.polytech.bmh.data.model.SignInBody
import com.polytech.bmh.data.model.SignUpBody
import com.polytech.bmh.service.RetrofitInstance
import com.polytech.bmh.service.SignInUser
import com.polytech.bmh.service.SignUpUser
import com.polytech.bmh.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_new_account.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewAccountActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)

        val lastName = findViewById<EditText>(R.id.editTextLastName)
        val firstName = findViewById<EditText>(R.id.editTextFirstName)
        val sex = findViewById<Spinner>(R.id.spinnerSex)
        val age = findViewById<EditText>(R.id.editTextAge)
        val email = findViewById<EditText>(R.id.editTextEmail)
        val password = findViewById<EditText>(R.id.editTextPassword)
        val address = findViewById<EditText>(R.id.editTextAddress)
        val city = findViewById<EditText>(R.id.editTextCity)
        val country = findViewById<Spinner>(R.id.spinnerCountry)

        var sexDifferentValues = arrayOf("Homme", "Femme")

        //sex!!.setOnItemSelectedListener(this)

        var arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sexDifferentValues)
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //sex.setAdapter(arrayAdapter)

        sex?.adapter = arrayAdapter
        sex?.onItemSelectedListener = this


        var countryDifferentValues = arrayOf("France", "Belgique", "Angleterre", "Allemagne", "Espagne", "Portugal" )
        var arrayAdapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryDifferentValues)
        country?.adapter = arrayAdapter2
        country?.onItemSelectedListener = this






/*with(sex)
{
    adapter = arrayAdapter
    setSelection(0, false)
    //onItemSelectedListener = this@NewAccountActivity
    //layoutParams = ll
    prompt = "Sexe"
    setPopupBackgroundResource(R.color.colorPrimary)

}*/



    val backArrow = findViewById<ImageView>(R.id.imageView5)

backArrow.setOnClickListener {
    val LoginActivityIntent = Intent(this, LoginActivity::class.java)
    startActivity(LoginActivityIntent)
}

val btCreateAccount = findViewById<Button>(R.id.buttonCreateAccount)

btCreateAccount.setOnClickListener {

    val lastNameValue = lastName.text.toString()
    val firstNameValue = firstName.text.toString()
    val sexValue = sex.selectedItem.toString()
    var ageValue = 0
    if (age.text.toString() != "") {
        ageValue = age.text.toString().toInt()
    }
    //val ageValue = age.text.toString().toInt()
    val emailValue = email.text.toString()
    val passwordValue = password.text.toString()
    val addressValue = address.text.toString()
    val cityValue = city.text.toString()
    val countryValue = country.selectedItem.toString()

    var sexBooleanValue: Boolean = false
    if (sexValue == "Femme") {
        sexBooleanValue = true
    }

    if (lastNameValue.isEmpty()) {
        lastName.error = "Le nom est requis !"
        lastName.requestFocus()
        val myToast = Toast.makeText(this, "Le nom est requis !", Toast.LENGTH_SHORT)
        myToast.show()
        return@setOnClickListener
    }
    if (firstNameValue.isEmpty()) {
        firstName.error = "Le prénom est requis !"
        firstName.requestFocus()
        val myToast = Toast.makeText(this, "Le prénom est requis !", Toast.LENGTH_SHORT)
        myToast.show()
        return@setOnClickListener
    }

    if (sexValue.isEmpty()) {
        sex.prompt = "Le sexe est requis !"
        sex.requestFocus()
        val myToast = Toast.makeText(this, "Le sexe est requis !", Toast.LENGTH_SHORT)
        myToast.show()
        return@setOnClickListener
    }

    if (ageValue.toString().isEmpty()) {
        age.error = "L'âge est requis !"
        age.requestFocus()
        val myToast = Toast.makeText(this, "L'âge est requis !", Toast.LENGTH_SHORT)
        myToast.show()
        return@setOnClickListener
    }

    if (ageValue < 1) {
        age.error = "L'âge doit être supérieur à 1 !"
        age.requestFocus()
        val myToast = Toast.makeText(this, "L'âge doit être supérieur à 1 !", Toast.LENGTH_SHORT)
        myToast.show()
        return@setOnClickListener
    }

    if (ageValue > 150) {
        age.error = "L'âge doit être au maximum égal à 150 !"
        age.requestFocus()
        val myToast = Toast.makeText(this, "L'âge doit être au maximum égal à 150 !", Toast.LENGTH_SHORT)
        myToast.show()
        return@setOnClickListener
    }

    if (emailValue.isEmpty()) {
        email.error = "L'email est requis !"
        email.requestFocus()
        val myToast = Toast.makeText(this, "L'email est requis !", Toast.LENGTH_SHORT)
        myToast.show()
        return@setOnClickListener
    }

    if (passwordValue.isEmpty()) {
        password.error = "Le mot de passe est requis !"
        password.requestFocus()
        val myToast = Toast.makeText(this, "Le mot de passe est requis !", Toast.LENGTH_SHORT)
        myToast.show()
        return@setOnClickListener
    }

    if (passwordValue.length < 6) {
        password.error = "Le mot de passe doit contenir au moins 6 caractères !"
        password.requestFocus()
        val myToast = Toast.makeText(this, "Le mot de passe doit contenir au moins 6 caractères !", Toast.LENGTH_SHORT)
        myToast.show()
        return@setOnClickListener
    }

    if (passwordValue.length > 32) {
        password.error = "Le mot de passe doit contenir au plus 32 caractères !"
        password.requestFocus()
        val myToast = Toast.makeText(this, "Le mot de passe doit contenir au plus 32 caractères !", Toast.LENGTH_SHORT)
        myToast.show()
        return@setOnClickListener
    }

    if (addressValue.isEmpty()) {
        address.error = "L'adresse est requise !"
        address.requestFocus()
        val myToast = Toast.makeText(this, "L'adresse est requise !", Toast.LENGTH_SHORT)
        myToast.show()
        return@setOnClickListener
    }

    if (cityValue.isEmpty()) {
        city.error = "Le nom de la ville est requis !"
        city.requestFocus()
        val myToast = Toast.makeText(this, "Le nom de la ville est requis !", Toast.LENGTH_SHORT)
        myToast.show()
        return@setOnClickListener
    }

    if (countryValue.isEmpty()) {
        country.prompt = "Le pays est requis !"
        country.requestFocus()
        val myToast = Toast.makeText(this, "Le pays est requis !", Toast.LENGTH_SHORT)
        myToast.show()
        return@setOnClickListener
    }


    signUp(lastNameValue, firstNameValue, sexBooleanValue, ageValue, emailValue, passwordValue, addressValue, cityValue, countryValue)


}


}

private fun signUp(lastName: String, firstName: String, sex: Boolean, age: Int, email: String, password: String, address: String, city: String, country: String ) {
val service = RetrofitInstance.getRetrofitInstance().create(SignUpUser::class.java)
val signUpRequest = service.signUp(SignUpBody(lastName, firstName, sex, age, email, password, address, city, country))

signUpRequest.enqueue(object : Callback<ResponseBody> {

    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
        if (response.code() == 201) {
            Toast.makeText(this@NewAccountActivity, "Le compte a été créé avec succès", Toast.LENGTH_SHORT)
                .show()

            val LoginActivityIntent = Intent(this@NewAccountActivity, LoginActivity::class.java)
            startActivity(LoginActivityIntent)


        } else {
            Toast.makeText(
                this@NewAccountActivity,
                "Le compte ne peut pas être créé",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
        Toast.makeText(
            this@NewAccountActivity,
            t.message,
            Toast.LENGTH_SHORT
        ).show()
    }

})
}

override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    var items: String = parent?.getItemAtPosition(position) as String
    Toast.makeText(applicationContext, "$items", Toast.LENGTH_LONG).show()
}

override fun onNothingSelected(p0: AdapterView<*>?) {
    Toast.makeText(applicationContext, "Nothig select", Toast.LENGTH_LONG).show()

}
}