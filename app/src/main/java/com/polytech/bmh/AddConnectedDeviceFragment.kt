package com.polytech.bmh

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
import com.polytech.bmh.databinding.FragmentAddConnectedDeviceBinding
import com.polytech.bmh.service.*
import com.polytech.bmh.viewmodel.AddConnectedDeviceViewModel
import com.polytech.bmh.viewmodelfactory.AddConnectedDeviceViewModelFactory

class AddConnectedDeviceFragment : Fragment() {

    private lateinit var binding: FragmentAddConnectedDeviceBinding
    private lateinit var viewModel: AddConnectedDeviceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_connected_device,
            container,
            false
        )

        val viewModelFactory = AddConnectedDeviceViewModelFactory()
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(AddConnectedDeviceViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.apply {
            textViewAddConnectedDevice.text = getString(R.string.add_connected_device)
            editTextObjectName.hint = getString(R.string.connected_device_name)
            editTextObjectDescription.hint = getString(R.string.connected_device_description)
            editTextObjectRouter.hint = getString(R.string.connected_device_router)
            buttonCreateNewConnectedDevice.text = getString(R.string.create_new_connected_device)
        }

        val connectedDeviceName = binding.editTextObjectName
        val connectedDeviceDescription = binding.editTextObjectDescription
        val connectedDeviceRouter = binding.editTextObjectRouter



        binding.imageViewBackArrow.setOnClickListener {
            this.findNavController().navigate(R.id.action_addConnectedDeviceFragment_to_choiceConnectedDeviceFragment)
        }


        binding.buttonCreateNewConnectedDevice.setOnClickListener {

            val connectedDeviceNameValue = connectedDeviceName.text.toString()
            val connectedDeviceDescriptionValue = connectedDeviceDescription.text.toString()
            val connectedDeviceRouterValue = connectedDeviceRouter.text.toString()

            viewModel.addConnectedDeviceFormChanged(connectedDeviceNameValue, connectedDeviceDescriptionValue, connectedDeviceRouterValue)

            viewModel.addConnectedDevice(connectedDeviceNameValue, connectedDeviceDescriptionValue, connectedDeviceRouterValue)

        }

        viewModel.response.observe(viewLifecycleOwner, Observer {
            val response = it ?: return@Observer

            if(response.success != null) {
                updateUI(response.success)
            }
            if(response.error != null) {
                addConnectedDeviceFailed(response.error)
            }
        })

        viewModel.addConnectedDeviceBodyState.observe(viewLifecycleOwner, Observer {
            val connectedDeviceValidate = it ?: return@Observer

            if(connectedDeviceValidate.nameError != null) {
                binding.editTextObjectName.error = connectedDeviceValidate.nameError
                binding.editTextObjectName.requestFocus()
                Toast.makeText(
                    this.context,
                    "${connectedDeviceValidate.nameError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            if(connectedDeviceValidate.descriptionError != null) {
                binding.editTextObjectDescription.error = connectedDeviceValidate.descriptionError
                binding.editTextObjectDescription.requestFocus()
                Toast.makeText(
                    this.context,
                    "${connectedDeviceValidate.descriptionError}",
                    Toast.LENGTH_LONG
                ).show()
            }
            if(connectedDeviceValidate.routerError != null) {
                binding.editTextObjectRouter.error = connectedDeviceValidate.routerError
                binding.editTextObjectRouter.requestFocus()
                Toast.makeText(
                    this.context,
                    "${connectedDeviceValidate.routerError}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })




        return binding.root
    }

    private fun updateUI(success: String) {
        Toast.makeText(
            this.context,
            "$success",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun addConnectedDeviceFailed(error: String) {
        val errorName = error.toString()
        Toast.makeText(
            this.context,
            "$errorName",
            Toast.LENGTH_LONG
        ).show()

        this.findNavController().navigate(R.id.action_loginFragment_self)
    }
}
