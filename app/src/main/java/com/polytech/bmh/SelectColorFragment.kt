package com.polytech.bmh

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
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
import com.polytech.bmh.viewmodel.ChoiceConnectedDeviceViewModel
import com.polytech.bmh.viewmodel.LoginViewModel
import com.polytech.bmh.viewmodel.SelectColorViewModel
import com.polytech.bmh.viewmodelfactory.LoginViewModelFactory
import com.polytech.bmh.viewmodelfactory.SelectColorViewModelFactory
import top.defaults.colorpicker.ColorPickerPopup

class SelectColorFragment : Fragment() {

    private lateinit var binding: FragmentSelectColorBinding
    private lateinit var viewModel: SelectColorViewModel
    private lateinit var viewModelChoiceConnectedDevice: ChoiceConnectedDeviceViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?) : View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_color, container, false)

        val viewModelFactory = SelectColorViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(SelectColorViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.apply {
            textViewChooseColor.text = getString(R.string.choose_color)
            textViewSubtitle.text = getString(R.string.selected_connected_device)
            buttonChooseColor.text = getString(R.string.change_leds_color)
            textViewSelectedColor.text = getString(R.string.current_color_leds)
            buttonValidateColor.text = getString(R.string.validate_leds_color)

        }

        var defaultColor = 0
        var redRgb = 0
        var greenRgb = 0
        var blueRgb = 0

        binding.backArrowImage.setOnClickListener {
            this.findNavController().navigate(SelectColorFragmentDirections.actionSelectColorFragmentToChoiceConnectedDeviceFragment())
        }

        val args = SelectColorFragmentArgs.fromBundle(requireArguments())
        var connectedDeviceSelectedId = args.connectedDeviceId
        //var connectedDeviceSelected = args.connectedDevice

        viewModel.getConnectedDeviceById(connectedDeviceSelectedId)

        viewModel.connectedDevice.observe(viewLifecycleOwner, Observer {
            val connectedDevice = it ?: return@Observer

            val intRed = connectedDevice.state.led_state.red_value
            val intGreen = connectedDevice.state.led_state.green_value
            val intBlue = connectedDevice.state.led_state.blue_value
            val color = Color.rgb(intRed, intGreen, intBlue)

            defaultColor = color

            binding.viewColorSelected.setBackgroundColor(defaultColor)

            binding.textViewNameConnectedDevice.text = "Nom : ${connectedDevice.name}"
            binding.textViewRouterConnectedDevice.text = "Adresse IP : ${connectedDevice.router}"
        })

        viewModel.response.observe(viewLifecycleOwner, Observer {
            val response = it ?: return@Observer
        })

        var colorPicker = object: ColorPickerPopup.ColorPickerObserver() {
            override fun onColorPicked(color: Int) {
                defaultColor = color
                binding.viewColorSelected.setBackgroundColor(defaultColor)

                redRgb = Color.red(color)
                greenRgb = Color.green(color)
                blueRgb = Color.blue(color)

                binding.buttonValidateColor.setOnClickListener {
                    viewModel.updateConnectedDevice(connectedDeviceSelectedId, redRgb, greenRgb, blueRgb)
                }

            }

        }

        var chooseColor = object: View.OnClickListener {
            override fun onClick(v: View?) {
                ColorPickerPopup.Builder(context).initialColor(Color.RED).
                enableBrightness(true).enableAlpha(true).okTitle("Valider").
                cancelTitle("Annuler").showIndicator(true).showValue(true).
                build().show(v, colorPicker)

            }

        }

        binding.buttonChooseColor.setOnClickListener {
            chooseColor.onClick(view)
        }


        return binding.root
        }



    }
