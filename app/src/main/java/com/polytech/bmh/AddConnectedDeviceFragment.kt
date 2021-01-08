package com.polytech.bmh

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.polytech.bmh.data.model.ConnectedDeviceAddBody
import com.polytech.bmh.databinding.FragmentAddConnectedDeviceBinding
import com.polytech.bmh.service.*
import com.polytech.bmh.ui.login.LoggedInUserView
import com.polytech.bmh.viewmodel.AddConnectedDeviceViewModel
import com.polytech.bmh.viewmodel.ChoiceConnectedDeviceViewModel
import com.polytech.bmh.viewmodelfactory.AddConnectedDeviceViewModelFactory
import com.polytech.bmh.viewmodelfactory.ChoiceConnectedDeviceViewModelFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

           /* if(connectedDeviceNameValue.isEmpty()) {
                connectedDeviceName.error = "Le nom de l'objet connecté est requis !"
                connectedDeviceName.requestFocus()
                val myToast = Toast.makeText(this.context, "Le nom de l'objet connecté est requis !", Toast.LENGTH_SHORT)
                myToast.show()
                return@setOnClickListener
            }

            if(connectedDeviceDescriptionValue.isEmpty()) {
                connectedDeviceDescription.error = "La description de l'objet connecté est requise !"
                connectedDeviceDescription.requestFocus()
                val myToast = Toast.makeText(this.context, "La description de l'objet connecté est requise !", Toast.LENGTH_SHORT)
                myToast.show()
                return@setOnClickListener
            }

            if(connectedDeviceRouterValue.isEmpty()) {
                connectedDeviceRouter.error = "La valeur du routeur de l'objet connecté est requis !"
                connectedDeviceRouter.requestFocus()
                val myToast = Toast.makeText(this.context, "La valeur du routeur de l'objet connecté est requis !", Toast.LENGTH_SHORT)
                myToast.show()
                return@setOnClickListener
            }

            addObject(connectedDeviceNameValue, connectedDeviceDescriptionValue, connectedDeviceRouterValue) */

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

        /*viewModel.addConnectedDeviceBody.observe(viewLifecycleOwner, Observer {

            val connectedDeviceBody = it ?: return@Observer

            if (connectedDeviceBody != null) {

            }

        })*/

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

        //this.findNavController().navigate(R.id.action_addConnectedDeviceFragment_to_choiceConnectedDeviceFragment)
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

    /*private fun addObject(name: String, description: String, router: String) {
        val service = RetrofitInstance.getRetrofitInstance().create(AddConnectedDevice::class.java)
        val addConnectedDeviceRequest = service.addConnectedDevice(ConnectedDeviceAddBody(name, description, router))

        addConnectedDeviceRequest.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 201) {
                    Toast.makeText(this@AddConnectedDeviceFragment,
                        "L'object connecté $name a été créé avec succès", Toast.LENGTH_SHORT)
                        .show()

                    val ChoiceConnectedDeviceActivityIntent = Intent(this@AddConnectedDeviceFragment, ChoiceConnectedDeviceFragment::class.java)
                    startActivity(ChoiceConnectedDeviceActivityIntent)

                } else {
                    val errorMessage = Gson().fromJson(response.body()?.string().toString(), MessageError::class.java).message.message
                    Toast.makeText(
                        this@AddConnectedDeviceFragment,
                        "L'objet connecté ne peut pas être créé : $errorMessage",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@AddConnectedDeviceFragment,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }*/
}
