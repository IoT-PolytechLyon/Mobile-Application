package com.polytech.bmh

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.polytech.bmh.data.model.user.signup.SignUpBody
import com.polytech.bmh.databinding.FragmentNewAccountBinding
import com.polytech.bmh.service.RetrofitInstance
import com.polytech.bmh.service.SignUpUser
import com.polytech.bmh.ui.login.LoginFragment
import com.polytech.bmh.viewmodel.NewAccountViewModel
import com.polytech.bmh.viewmodelfactory.NewAccountViewModelFactory
import kotlinx.android.synthetic.main.fragment_new_account.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewAccountFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentNewAccountBinding
    private lateinit var viewModel: NewAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_account,
            container,
            false
        )

        val viewModelFactory = NewAccountViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewAccountViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this



        viewModel.signUpFormState.observe(viewLifecycleOwner, Observer {
            val newUserValidate = it ?: return@Observer

            if (newUserValidate.lastNameError != null) {
                binding.editTextLastName.error = newUserValidate.lastNameError
                binding.editTextLastName.requestFocus()
                Toast.makeText(
                    this.context,
                    "${newUserValidate.lastNameError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (newUserValidate.firstNameError != null) {
                binding.editTextFirstName.error = newUserValidate.firstNameError
                binding.editTextFirstName.requestFocus()
                Toast.makeText(
                    this.context,
                    "${newUserValidate.firstNameError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (newUserValidate.ageError != null) {
                binding.editTextAge.error = newUserValidate.ageError
                binding.editTextAge.requestFocus()
                Toast.makeText(
                    this.context,
                    "${newUserValidate.ageError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (newUserValidate.emailError != null) {
                binding.editTextEmail.error = newUserValidate.emailError
                binding.editTextEmail.requestFocus()
                Toast.makeText(
                    this.context,
                    "${newUserValidate.emailError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (newUserValidate.passwordError != null) {
                binding.editTextPassword.error = newUserValidate.passwordError
                binding.editTextPassword.requestFocus()
                Toast.makeText(
                    this.context,
                    "${newUserValidate.passwordError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (newUserValidate.addressError != null) {
                binding.editTextAddress.error = newUserValidate.addressError
                binding.editTextAddress.requestFocus()
                Toast.makeText(
                    this.context,
                    "${newUserValidate.addressError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (newUserValidate.cityError != null) {
                binding.editTextCity.error = newUserValidate.cityError
                binding.editTextCity.requestFocus()
                Toast.makeText(
                    this.context,
                    "${newUserValidate.cityError}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        viewModel.signUpResponse.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer

            if (result == null) {
                signUpFailed()
            } else {
                signUpSuccess()
            }
        })

        binding.buttonCreateAccount.setOnClickListener {

            val lastName = binding.editTextLastName.text.toString()
            val firstName = binding.editTextFirstName.text.toString()
            val sex = binding.spinnerSex.toString()
            val age = binding.editTextAge.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val address = binding.editTextAddress.text.toString()
            val city = binding.editTextCity.text.toString()
            val country = binding.spinnerCountry.toString()

            viewModel.signUpFormValidate(lastName, firstName, age, email, password, address, city, country)

            if (viewModel.signUpFormState.value!!.isDataValid) {
                viewModel.signUp(lastName, firstName, sex, age, email, password, address, city, country)
            }
        }

        var arrayFirstAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, viewModel.listOfSex())
        arrayFirstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerSex?.adapter = arrayFirstAdapter
        binding.spinnerSex?.onItemSelectedListener = this

        var arraySecondAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, viewModel.listOfCountries())
        arraySecondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerCountry?.adapter = arraySecondAdapter
        binding.spinnerCountry?.onItemSelectedListener = this

        return binding.root
    }

    fun signUpSuccess() {
        Toast.makeText(
            this.context,
            "Le compte a été créé ! Merci de vous connecter !",
            Toast.LENGTH_LONG
        ).show()
        this.findNavController().navigate(NewAccountFragmentDirections.actionNewAccountFragmentToLoginFragment())
    }

    fun signUpFailed() {
        Toast.makeText(
            this.context,
            "Erreur lors de la création du compte !",
            Toast.LENGTH_LONG
        ).show()

        this.findNavController().navigate(NewAccountFragmentDirections.actionNewAccountFragmentSelf())
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items: String = parent?.getItemAtPosition(position) as String
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(this.context, "Nothing select", Toast.LENGTH_LONG).show()
    }

    /* var sexDifferentValues = arrayOf("Homme", "Femme")

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




 val backArrow = findViewById<ImageView>(R.id.imageViewBackArrow)

backArrow.setOnClickListener {
 val LoginActivityIntent = Intent(this, LoginFragment::class.java)
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
         Toast.makeText(this@NewAccountFragment, "Le compte a été créé avec succès", Toast.LENGTH_SHORT)
             .show()

         val LoginActivityIntent = Intent(this@NewAccountFragment, LoginFragment::class.java)
         startActivity(LoginActivityIntent)


     } else {
         Toast.makeText(
             this@NewAccountFragment,
             "Le compte ne peut pas être créé",
             Toast.LENGTH_SHORT
         )
             .show()
     }
 }

 override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
     Toast.makeText(
         this@NewAccountFragment,
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

}*/
}