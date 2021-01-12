package com.polytech.bmh

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
import com.polytech.bmh.data.Result
import com.polytech.bmh.data.model.user.signup.SignUpBody
import com.polytech.bmh.databinding.FragmentNewAccountBinding
import com.polytech.bmh.viewmodel.NewAccountViewModel
import com.polytech.bmh.viewmodelfactory.NewAccountViewModelFactory
import kotlinx.android.synthetic.main.fragment_new_account.*

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

        binding.apply {
            textViewTitleCreateNewAccount.text = getString(R.string.title_create_new_account)
            editTextLastName.hint = getString(R.string.user_last_name)
            editTextFirstName.hint = getString(R.string.user_first_name)
            editTextAge.hint = getString(R.string.user_age)
            editTextEmail.hint = getString(R.string.user_email)
            editTextPassword.hint = getString(R.string.user_password)
            editTextAddress.hint = getString(R.string.user_address)
            editTextCity.hint = getString(R.string.user_city)
            buttonCreateAccount.text = getString(R.string.create_new_account)
        }



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

            if (result is Result.Success) {
                signUpSuccess(result)
            } else {
                signUpFailed(result)
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

        binding.spinnerCountry.setPromptId(R.string.create_new_account)

        binding.imageViewBackArrow.setOnClickListener {
            this.findNavController().navigate(NewAccountFragmentDirections.actionNewAccountFragmentToLoginFragment())
        }

        return binding.root
    }

    private fun signUpSuccess(result: Result<SignUpBody>) {
        Toast.makeText(
            this.context,
            "Le compte ${(result as Result.Success).data.email} a été créé ! Merci de vous connecter !",
            Toast.LENGTH_LONG
        ).show()
        this.findNavController().navigate(NewAccountFragmentDirections.actionNewAccountFragmentToLoginFragment())
    }

    private fun signUpFailed(result: Result<SignUpBody>) {
        Toast.makeText(
            this.context,
            (result as Result.Error).exception.message,
            Toast.LENGTH_LONG
        ).show()

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items: String = parent?.getItemAtPosition(position) as String
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(this.context, "Nothing select", Toast.LENGTH_LONG).show()
    }

}