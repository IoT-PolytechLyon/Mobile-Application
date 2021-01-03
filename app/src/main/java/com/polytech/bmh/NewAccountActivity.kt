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
    //val sexValue = sex.text.toString()
    val emailValue = email.text.toString()
    val passwordValue = password.text.toString()
    val addressValue = address.text.toString()
    val cityValue = city.text.toString()
    //val countryValue = country.text.toString()



}


}

private fun signUp(lastName: String, firstName: String, sex: Boolean, age: Number, email: String, password: String, address: String, city: String, country: String  ) {
val service = RetrofitInstance.getRetrofitInstance().create(SignUpUser::class.java)
val signInRequest = service.signUp(SignUpBody(lastName, firstName, sex, age, email, password, address, city, country))

signInRequest.enqueue(object : Callback<ResponseBody> {

    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
        if (response.code() == 200) {
            Toast.makeText(this@NewAccountActivity, "Login success!", Toast.LENGTH_SHORT)
                .show()

            val ChoiceConnectedDeviceActivityIntent = Intent(this@NewAccountActivity, ChoiceConnectedDeviceActivity::class.java)
            startActivity(ChoiceConnectedDeviceActivityIntent)


        } else {
            Toast.makeText(
                this@NewAccountActivity,
                "L'email ou/et le mot de passe est/sont incorrects",
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