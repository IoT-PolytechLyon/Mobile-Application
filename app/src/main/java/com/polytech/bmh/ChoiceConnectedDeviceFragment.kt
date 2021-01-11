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
import com.polytech.bmh.data.database.Database
import com.polytech.bmh.data.database.dao.ConnectedDeviceDao
import com.polytech.bmh.databinding.FragmentChoiceConnectedDeviceBinding
import com.polytech.bmh.viewmodel.ChoiceConnectedDeviceViewModel
import com.polytech.bmh.viewmodelfactory.ChoiceConnectedDeviceViewModelFactory

class ChoiceConnectedDeviceFragment : Fragment() {

    private lateinit var binding: FragmentChoiceConnectedDeviceBinding
    private lateinit var viewModel: ChoiceConnectedDeviceViewModel
    private lateinit var dataSource: ConnectedDeviceDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_choice_connected_device,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        dataSource = Database.getInstance(application).connectedDevicesDao
        val viewModelFactory = ChoiceConnectedDeviceViewModelFactory(dataSource)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(ChoiceConnectedDeviceViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.apply {
            textViewConnectedDeviceChoice.text = getString(R.string.connected_device_choice)
            buttonConnectedDeviceChoice.text = getString(R.string.button_to_choose_a_connected_device)
        }

        viewModel.getConnectedDevices()

        viewModel.response.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer
        })

        /*viewModel.connectedDevice.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer

            viewModel.getListConnectedDevicesUI(result)

            viewModel.connectedDeviceListUi.observe(viewLifecycleOwner, Observer {
                val listConnectedDeviceUi = it ?: return@Observer

                var arrayAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, listConnectedDeviceUi )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                //binding.spinnerConnectedDeviceChoice?.adapter = arrayAdapter
                //binding.spinnerConnectedDeviceChoice?.onItemSelectedListener = this

            })


        })*/
        binding.imageBackArrow.setOnClickListener {
            this.findNavController().navigate(ChoiceConnectedDeviceFragmentDirections.actionChoiceConnectedDeviceFragmentToLoginFragment())
        }

        binding.buttonAddConnectedDevice.setOnClickListener {
            this.findNavController().navigate(ChoiceConnectedDeviceFragmentDirections.actionChoiceConnectedDeviceFragmentToAddConnectedDeviceFragment())
        }

        /*binding.buttonValidateConnectedDevice.setOnClickListener {
           // this.findNavController().navigate(ChoiceConnectedDeviceFragmentDirections.
            //actionChoiceConnectedDeviceFragmentToSelectColorFragment(viewModel.connectedDeviceSelected.value!!))
        }*/

        binding.buttonConnectedDeviceChoice.setOnClickListener {
            this.findNavController().navigate(ChoiceConnectedDeviceFragmentDirections.actionChoiceConnectedDeviceFragmentToListFragment())
        }

        return binding.root
    }

}
