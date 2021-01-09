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

        var defaultColor = 0
        var redRgb = 0
        var greenRgb = 0
        var blueRgb = 0

        binding.backArrowImage.setOnClickListener {
            this.findNavController().navigate(SelectColorFragmentDirections.actionSelectColorFragmentToChoiceConnectedDeviceFragment())
        }

        val args = SelectColorFragmentArgs.fromBundle(requireArguments())
        var connectedDeviceSelected = args.connectedDevice
        var tabEachWord = connectedDeviceSelected.split(" ")
        var idConnectedDeviceSelected = tabEachWord[2]

        viewModel.getConnectedDeviceById(idConnectedDeviceSelected)

        viewModel.connectedDevice.observe(viewLifecycleOwner, Observer {
            val connectedDevice = it ?: return@Observer

            val intRed = connectedDevice.state.led_state.red_value
            val intGreen = connectedDevice.state.led_state.green_value
            val intBlue = connectedDevice.state.led_state.blue_value
            val color = Color.rgb(intRed, intGreen, intBlue)

            defaultColor = color

            binding.viewColorSelected.setBackgroundColor(defaultColor)

        })


        //viewModel.getConnectedDeviceById(idConnectedDeviceSelected)

        /*viewModel.connectedDevice.observe(viewLifecycleOwner, Observer {
            val connectedDeviceCurrent = it ?: return@Observer

            Toast.makeText(
                this.context,
                "${connectedDeviceCurrent._id} ${connectedDeviceCurrent.name} ${connectedDeviceCurrent.router}",
                Toast.LENGTH_LONG
            ).show()
        })*/

        viewModel.response.observe(viewLifecycleOwner, Observer {
            val response = it ?: return@Observer

            Toast.makeText(
                this.context,
                "$response",
                Toast.LENGTH_LONG
            ).show()
        })

        var colorPicker = object: ColorPickerPopup.ColorPickerObserver() {
            override fun onColorPicked(color: Int) {
                defaultColor = color
                binding.viewColorSelected.setBackgroundColor(defaultColor)


                //var redRgb = Color.red(color)
                //var greenRgb = Color.green(color)
                //var blueRgb = Color.blue(color)

                redRgb = Color.red(color)
                greenRgb = Color.green(color)
                blueRgb = Color.blue(color)

                /*if(binding.checkBoxWhiteColor.isChecked) {
                    redRgb = 255
                    greenRgb = 255
                    blueRgb = 255
                    defaultColor = Color.rgb(redRgb, greenRgb, blueRgb)
                }*/

                binding.buttonValidateColor.setOnClickListener {
                    if(binding.checkBoxWhiteColor.isActivated) {
                        redRgb = 255
                        greenRgb = 255
                        blueRgb = 255
                        defaultColor = Color.rgb(redRgb, greenRgb, blueRgb)
                    }
                    viewModel.updateConnectedDevice(idConnectedDeviceSelected, redRgb, greenRgb, blueRgb)
                }

                /*if(binding.checkBoxWhiteColor.isActivated) {
                    defaultColor = Color.rgb(255, 255, 255)
                    binding.viewColorSelected.setBackgroundColor(defaultColor)
                }*/


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



        /*binding.buttonValidateColor.setOnClickListener {
            if(binding.checkBoxWhiteColor.isChecked) {
                redRgb = 255
                greenRgb = 255
                blueRgb = 255
                defaultColor = Color.rgb(redRgb, greenRgb, blueRgb)
                viewModel.updateConnectedDevice(
                    idConnectedDeviceSelected,
                    redRgb,
                    greenRgb,
                    blueRgb
                )
            }
        }*/






        binding.buttonChooseColor.setOnClickListener {
            chooseColor.onClick(view)


                }
        return binding.root
        }



    }
