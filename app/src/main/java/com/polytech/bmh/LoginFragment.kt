package com.polytech.bmh

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController


import com.polytech.bmh.databinding.FragmentLoginBinding
import com.polytech.bmh.data.model.user.signin.LoggedInUserView
import com.polytech.bmh.ui.login.LoginFragmentDirections
import com.polytech.bmh.viewmodel.LoginViewModel
import com.polytech.bmh.viewmodelfactory.LoginViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?) : View? {

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

        viewModel.loginResponseBody.observe(viewLifecycleOwner, Observer {

            val loginRes = it ?: return@Observer

            if (loginRes.error != null) {
                loginFailed(loginRes.error)
            }
            if (loginRes.success != null) {
                updateUI(loginRes.success)
            }
           // setResult(Activity.RESULT_OK)

            //finish()
        })

        viewModel.signInFormBodyState.observe(viewLifecycleOwner, Observer {
            val userValidate = it ?: return@Observer

            if (userValidate.emailError != null) {
                binding.editTextEmail.error = userValidate.emailError
                binding.editTextEmail.requestFocus()
                Toast.makeText(
                    this.context,
                    "${userValidate.emailError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (userValidate.passwordError != null) {
                binding.editTextPassword.error = userValidate.passwordError
                binding.editTextPassword.requestFocus()
                Toast.makeText(
                    this.context,
                    "${userValidate.passwordError}",
                    Toast.LENGTH_LONG
                ).show()
            }


        })


                binding.buttonConnexion.setOnClickListener {
                    val email = binding.editTextEmail.text.toString()
                    val password = binding.editTextPassword.text.toString()

                    viewModel.signInFormValidate(email, password)

                    if (viewModel.signInFormBodyState.value!!.isDataValid) {
                        viewModel.signIn(email, password)
                    }


                }

                binding.buttonNewAccount.setOnClickListener {
                    this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToNewAccountFragment())
                }

        return binding.root

    }

    private fun loginFailed(error: String) {
        val errorName = error.toString()
        Toast.makeText(
            this.context,
            "$errorName",
            Toast.LENGTH_LONG
        ).show()

        this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentSelf())
    }

    private fun updateUI(model: LoggedInUserView) {
        val displayName = model.displayName
        Toast.makeText(
            this.context,
            "Bienvenue  $displayName !",
            Toast.LENGTH_LONG
        ).show()


        hideKeyboard(activity as MainActivity)

        this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToChoiceConnectedDeviceFragment())
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val currentFocusedView = activity.currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}