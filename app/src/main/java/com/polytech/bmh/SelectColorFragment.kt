package com.polytech.bmh

import android.graphics.Color
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
import com.polytech.bmh.databinding.FragmentSelectColorBinding
import com.polytech.bmh.viewmodel.SelectColorViewModel
import com.polytech.bmh.viewmodelfactory.SelectColorViewModelFactory

class SelectColorFragment : Fragment() {

    private lateinit var binding: FragmentSelectColorBinding
    private lateinit var viewModel: SelectColorViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_select_color, container, false)

        val viewModelFactory = SelectColorViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(SelectColorViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.apply {
            textViewChooseColor.text = getString(R.string.choose_color)
            textViewSubtitle.text = getString(R.string.selected_connected_device)
            textViewSelectedColor.text = getString(R.string.current_color_leds)
        }

        // default values
        var defaultColor = 0
        var redRgb = 0
        var greenRgb = 0
        var blueRgb = 0

        // we retrieve the id connected device on which the user has clicked (from the recycler view)
        val args = SelectColorFragmentArgs.fromBundle(requireArguments())
        var connectedDeviceSelectedId = args.connectedDeviceId

        viewModel.getConnectedDeviceById(connectedDeviceSelectedId)

        binding.colorPicker.subscribe { color, fromUser, shouldPropagate ->

            // we display the current color in our view viewColorSelected
            binding.viewColorSelected.setBackgroundColor(color)

            redRgb = Color.red(color)
            greenRgb = Color.green(color)
            blueRgb = Color.blue(color)

            // updating leds color of the specific connected device
            viewModel.updateConnectedDevice(connectedDeviceSelectedId, redRgb, greenRgb, blueRgb)

        }

        viewModel.connectedDeviceSelected.observe(viewLifecycleOwner, Observer {
            val connectedDevice = it ?: return@Observer

            // we retrieve the color of the leds before the update
            val intRed = connectedDevice.state.led_state.red_value
            val intGreen = connectedDevice.state.led_state.green_value
            val intBlue = connectedDevice.state.led_state.blue_value
            val color = Color.rgb(intRed, intGreen, intBlue)

            defaultColor = color

            // we display the color in our view viewColorSelected
            binding.viewColorSelected.setBackgroundColor(defaultColor)
            // the picker is positioned at the color
            binding.colorPicker.setInitialColor(defaultColor)

            binding.textViewNameConnectedDevice.text = "Nom : ${connectedDevice.name}"
            binding.textViewRouterConnectedDevice.text = "Adresse IP : ${connectedDevice.router}"
        })

        // update response
        viewModel.updateLedsColorResponse.observe(viewLifecycleOwner, Observer {
            val updateResponse = it ?: return@Observer
            Toast.makeText(this.context, updateResponse, Toast.LENGTH_LONG)
        })


        // when clicking on the back arrow
        binding.imageViewBackArrow.setOnClickListener {
            this.findNavController()
                .navigate(SelectColorFragmentDirections.actionSelectColorFragmentToChoiceConnectedDeviceFragment())
        }

        return binding.root
    }

}
