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
import com.polytech.bmh.adapter.ConnectedDeviceListener
import com.polytech.bmh.adapter.MyListAdapter
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
        val viewModelFactory = ChoiceConnectedDeviceViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(ChoiceConnectedDeviceViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.apply {
            textViewConnectedDeviceChoice.text = getString(R.string.connected_device_choice)
        }

        // Recycler view
        val adapter = MyListAdapter(ConnectedDeviceListener {
                connectedDeviceId ->
            viewModel.onConnectedDeviceClicked(connectedDeviceId)
        })
        binding.recyclerViewList.adapter = adapter

        viewModel.connectedDevices.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.getConnectedDevices()

        // when clicking on the back arrow
        binding.imageBackArrow.setOnClickListener {
            this.findNavController().navigate(ChoiceConnectedDeviceFragmentDirections.actionChoiceConnectedDeviceFragmentToLoginFragment())
        }

        // when clicking on the adding connected device button
        binding.buttonAddConnectedDevice.setOnClickListener {
            this.findNavController().navigate(ChoiceConnectedDeviceFragmentDirections.actionChoiceConnectedDeviceFragmentToAddConnectedDeviceFragment())
        }

        // when clicking on a specific connected device
        viewModel.connectedDeviceSelectedId.observe(viewLifecycleOwner, Observer {
                connectedDevice -> connectedDevice?.let {
            this.findNavController().navigate(ChoiceConnectedDeviceFragmentDirections.actionChoiceConnectedDeviceFragmentToSelectColorFragment(connectedDevice))
        }
        })

        return binding.root
    }

}
