package com.polytech.bmh.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.polytech.bmh.R
import com.polytech.bmh.adapter.ConnectedDeviceListener
import com.polytech.bmh.adapter.MyListAdapter
import com.polytech.bmh.data.database.Database
import com.polytech.bmh.data.database.dao.ConnectedDeviceDao
import com.polytech.bmh.databinding.FragmentChoiceConnectedDeviceBinding
import com.polytech.bmh.viewmodel.ConnectedDeviceListChoiceViewModel
import com.polytech.bmh.viewmodelfactory.ConnectedDeviceListChoiceViewModelFactory

class ConnectedDeviceListChoiceFragment : Fragment() {

    private lateinit var binding: FragmentChoiceConnectedDeviceBinding
    private lateinit var viewModel: ConnectedDeviceListChoiceViewModel
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
        val viewModelFactory = ConnectedDeviceListChoiceViewModelFactory(dataSource)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(ConnectedDeviceListChoiceViewModel::class.java)

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
        binding.recyclerViewList

        viewModel.connectedDevices.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                if (it.isEmpty()) {
                    binding.textViewIfEmptyRecyclerView.text = getString(R.string.empty_recycler_view)
                } else {
                    binding.textViewIfEmptyRecyclerView.text = getString(R.string.nothing)
                }
                binding.loadingPanel.visibility = View.GONE
            }
        })

        viewModel.getConnectedDevices()

        // when clicking on the back arrow
        binding.imageBackArrow.setOnClickListener {
            this.findNavController().navigate(ConnectedDeviceListChoiceFragmentDirections.actionChoiceConnectedDeviceFragmentToLoginFragment())
        }

        // when clicking on the adding connected device button
        binding.buttonAddConnectedDevice.setOnClickListener {
            this.findNavController().navigate(ConnectedDeviceListChoiceFragmentDirections.actionChoiceConnectedDeviceFragmentToAddConnectedDeviceFragment())
        }

        // when clicking on a specific connected device
        viewModel.connectedDeviceSelectedId.observe(viewLifecycleOwner, Observer {
                connectedDevice -> connectedDevice?.let {
            this.findNavController().navigate(
                ConnectedDeviceListChoiceFragmentDirections.actionChoiceConnectedDeviceFragmentToSelectColorFragment(
                    connectedDevice
                )
            )
        }
        })

        return binding.root
    }

}
