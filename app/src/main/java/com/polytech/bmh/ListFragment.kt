package com.polytech.bmh

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.polytech.bmh.adapter.ConnectedDeviceListener
import com.polytech.bmh.adapter.MyListAdapter
import com.polytech.bmh.data.database.Database
import com.polytech.bmh.databinding.FragmentListBinding
import com.polytech.bmh.viewmodel.ListViewModel
import com.polytech.bmh.viewmodelfactory.ListViewModelFactory
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = Database.getInstance(application).connectedDevicesDao
        val viewModelFactory = ListViewModelFactory(dataSource, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = MyListAdapter(ConnectedDeviceListener {
                connectedDeviceId ->
            Toast.makeText(context, "${connectedDeviceId}", Toast.LENGTH_LONG).show()
            viewModel.onConnectedDeviceClicked(connectedDeviceId)
        })
        binding.recyclerViewList.adapter = adapter

        viewModel.connectedDevices.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.imageBackArrow.setOnClickListener {
            this.findNavController().navigate(R.id.action_listFragment_to_choiceConnectedDeviceFragment)
        }

        viewModel.connectedDeviceDetail.observe(viewLifecycleOwner, Observer {
            connectedDevice -> connectedDevice?.let {
                this.findNavController().navigate(ListFragmentDirections.actionListFragmentToSelectColorFragment(connectedDevice))
        }
        })

        //binding.recyclerViewList.addOnItemTouchListener(context, recyclerViewList, RecyclerItemz)

        return binding.root
    }

}