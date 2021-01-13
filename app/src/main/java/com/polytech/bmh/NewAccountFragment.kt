package com.polytech.bmh

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.polytech.bmh.data.Result
import com.polytech.bmh.data.model.user.signup.SignUpBody
import com.polytech.bmh.databinding.FragmentNewAccountBinding
import com.polytech.bmh.utils.Utils
import com.polytech.bmh.viewmodel.NewAccountViewModel
import com.polytech.bmh.viewmodelfactory.NewAccountViewModelFactory
import kotlinx.android.synthetic.main.fragment_new_account.*
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

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
            textViewBirthday.hint = getString(R.string.user_birthday)
            editTextEmail.hint = getString(R.string.user_email)
            editTextPassword.hint = getString(R.string.user_password)
            editTextAddress.hint = getString(R.string.user_address)
            editTextCity.hint = getString(R.string.user_city)
            buttonCreateAccount.text = getString(R.string.create_new_account)
            textViewYourAge.text = getString(R.string.your_age_default)
        }

        // loading panel invisible
        binding.loadingPanel.visibility = View.GONE

        var calendarDateOfBirth = Calendar.getInstance()

        // Date picker
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val sdf = viewModel.formattedDate(calendarDateOfBirth, year, monthOfYear, dayOfMonth)
            binding.textViewBirthday.text = sdf.format(calendarDateOfBirth.time)
            binding.textViewYourAge.text = "Votre âge : ${viewModel.dateOfBirthToAge(calendarDateOfBirth).toString()} ans"
        }


        // when clicking on the birthday view
        binding.textViewBirthday.setOnClickListener {
            val datePicker = DatePickerDialog(
                this.requireContext(), dateSetListener, calendarDateOfBirth.get(Calendar.YEAR),
                calendarDateOfBirth.get(Calendar.MONTH),
                calendarDateOfBirth.get(Calendar.DAY_OF_MONTH))
            // max date clickable
            datePicker.datePicker.maxDate = viewModel.maxDateVisible().timeInMillis
            // min date clickable
            datePicker.datePicker.minDate = viewModel.minDateVisible().timeInMillis
            datePicker.show()
        }




        viewModel.signUpFormState.observe(viewLifecycleOwner, Observer {
            val newUserValidate = it ?: return@Observer

            // if the last name does not respect the format
            if (newUserValidate.lastNameError != null) {
                binding.editTextLastName.error = newUserValidate.lastNameError
                binding.editTextLastName.requestFocus()
                Toast.makeText(
                    this.context,
                    "${newUserValidate.lastNameError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            // if the first name does not respect the format
            if (newUserValidate.firstNameError != null) {
                binding.editTextFirstName.error = newUserValidate.firstNameError
                binding.editTextFirstName.requestFocus()
                Toast.makeText(
                    this.context,
                    "${newUserValidate.firstNameError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            // if the age does not respect the format
            if (newUserValidate.ageError != null) {
                binding.textViewBirthday.error = newUserValidate.ageError
                binding.textViewBirthday.requestFocus()
                Toast.makeText(
                    this.context,
                    "${newUserValidate.ageError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            // if the email does not respect the format
            if (newUserValidate.emailError != null) {
                binding.editTextEmail.error = newUserValidate.emailError
                binding.editTextEmail.requestFocus()
                Toast.makeText(
                    this.context,
                    "${newUserValidate.emailError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            // if the password does not respect the format
            if (newUserValidate.passwordError != null) {
                binding.editTextPassword.error = newUserValidate.passwordError
                binding.editTextPassword.requestFocus()
                Toast.makeText(
                    this.context,
                    "${newUserValidate.passwordError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            // if the address does not respect the format
            if (newUserValidate.addressError != null) {
                binding.editTextAddress.error = newUserValidate.addressError
                binding.editTextAddress.requestFocus()
                Toast.makeText(
                    this.context,
                    "${newUserValidate.addressError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            // if the city does not respect the format
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

            // if there are no errors
            if (result is Result.Success) {
                signUpSuccess(result)

            // if there is an error
            } else {
                signUpFailed(result)
            }

        })

        // when clicking on the creating account button
        binding.buttonCreateAccount.setOnClickListener {

            val lastName = binding.editTextLastName.text.toString()
            val firstName = binding.editTextFirstName.text.toString()
            val sex = binding.spinnerGender.selectedItem.toString()
            val age = viewModel.dateOfBirthToAge(calendarDateOfBirth).toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val address = binding.editTextAddress.text.toString()
            val city = binding.editTextCity.text.toString()
            val country = binding.spinnerCountry.selectedItem.toString()

            viewModel.signUpFormValidate(lastName, firstName, age, email, password, address, city, country)

            // if all the data respect formats
            if (viewModel.signUpFormState.value!!.isDataValid) {
                binding.loadingPanel.visibility = View.VISIBLE
                viewModel.signUp(lastName, firstName, sex, age, email, password, address, city, country)
            }
        }

        // Gender spinner
        val arrayFirstAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, viewModel.listOfGender())
        arrayFirstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerGender.adapter = arrayFirstAdapter
        binding.spinnerGender.onItemSelectedListener = this

        // Countries spinner
        val arraySecondAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, viewModel.listOfCountries())
        arraySecondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerCountry.adapter = arraySecondAdapter
        binding.spinnerCountry.onItemSelectedListener = this

        binding.spinnerCountry.setPromptId(R.string.create_new_account)

        // when clicking on the back arrow button
        binding.imageViewBackArrow.setOnClickListener {
            this.findNavController().navigate(NewAccountFragmentDirections.actionNewAccountFragmentToLoginFragment())
            Utils.hideKeyboard(activity as Activity)
        }

        return binding.root
    }

    /**
     * When there is no error
     */
    private fun signUpSuccess(result: Result<SignUpBody>) {

        binding.loadingPanel.visibility = View.GONE

        Toast.makeText(
            this.context,
            "Le compte ${(result as Result.Success).data.email} a été créé ! Merci de vous connecter !",
            Toast.LENGTH_LONG
        ).show()
        this.findNavController().navigate(NewAccountFragmentDirections.actionNewAccountFragmentToLoginFragment())
    }

    /**
     * If there is an error during authentication
     */
    private fun signUpFailed(result: Result<SignUpBody>) {

        binding.loadingPanel.visibility = View.GONE

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