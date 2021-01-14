package com.polytech.bmh.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.polytech.bmh.R
import com.polytech.bmh.databinding.FragmentLoginBinding
import com.polytech.bmh.utils.Utils
import com.polytech.bmh.viewmodel.LoginViewModel
import com.polytech.bmh.viewmodelfactory.LoginViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        val viewModelFactory = LoginViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.apply {
            textViewIoT.text = getString(R.string.title_iot)
            textViewConnexion.text = getString(R.string.connexion)
            textViewEmail.text = getString(R.string.email)
            textViewPassword.text = getString(R.string.password)
            buttonConnexion.text = getString(R.string.connexion)
            buttonNewAccount.text = getString(R.string.creation_account)
        }

        binding.loadingPanel.visibility = View.GONE

        viewModel.signInResponse.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer

            // if there is an error during authentication
            if (loginResult.error != null) {
                loginFailed(loginResult.error)
            }
            // if there are no errors during authentication
            else if (loginResult.success != null) {
                loginSuccess(loginResult.success)
            // if there is an unexpected error
            } else {
                Toast.makeText(
                    this.context,
                    "Erreur innatendue !",
                    Toast.LENGTH_LONG
                ).show()
            }

        })

        viewModel.signInFormState.observe(viewLifecycleOwner, Observer {
            val userValidate = it ?: return@Observer

            // if the email does not respect the format
            if (userValidate.emailError != null) {
                binding.editTextEmail.error = userValidate.emailError
                binding.editTextEmail.requestFocus()
                Toast.makeText(
                    this.context,
                    "${userValidate.emailError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            // if the password does not respect the format
            if (userValidate.passwordError != null) {
                binding.editTextPassword.error = userValidate.passwordError
                binding.editTextPassword.requestFocus()
                Toast.makeText(
                    this.context,
                    "${userValidate.passwordError}",
                    Toast.LENGTH_LONG
                ).show()
            }

            if (userValidate.isDataValid) {
                binding.loadingPanel.visibility = View.VISIBLE
            }


        })

        // when clicking on the login button
        binding.buttonConnexion.setOnClickListener(object: View.OnClickListener {

            override fun onClick(view: View?) {
                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassword.text.toString()

                viewModel.signInFormValidate(email, password)

                // if all the data respect formats
                if (viewModel.signInFormState.value!!.isDataValid) {
                    viewModel.signIn(email, password)
                }
            }
        })

        // when clicking on the sign up button
        binding.buttonNewAccount.setOnClickListener {
            this.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToNewAccountFragment())
        }

        return binding.root

    }

    /**
     * When the user is well authenticated
     */
    private fun loginSuccess(success: String) {

        binding.loadingPanel.visibility = View.GONE

        Toast.makeText(
            this.context,
            success,
            Toast.LENGTH_SHORT
        ).show()

        Utils.hideKeyboard(activity as MainActivity)

        this.findNavController()
            .navigate(LoginFragmentDirections.actionLoginFragmentToChoiceConnectedDeviceFragment())
    }

    /**
     * If there is an error during authentication
     */
    private fun loginFailed(error: String) {

        binding.loadingPanel.visibility = View.GONE

        Toast.makeText(
            this.context,
            error,
            Toast.LENGTH_LONG
        ).show()
    }
}