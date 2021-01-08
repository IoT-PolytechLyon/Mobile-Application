package com.polytech.bmh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.polytech.bmh.databinding.FragmentSelectColorBinding
import com.polytech.bmh.viewmodel.LoginViewModel
import com.polytech.bmh.viewmodel.SelectColorViewModel
import com.polytech.bmh.viewmodelfactory.LoginViewModelFactory
import com.polytech.bmh.viewmodelfactory.SelectColorViewModelFactory

class SelectColorFragment : Fragment() {

    private lateinit var binding: FragmentSelectColorBinding
    private lateinit var viewModel: SelectColorViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?) : View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_color, container, false)

        val viewModelFactory = SelectColorViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(SelectColorViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.backArrowImage.setOnClickListener {
            this.findNavController().navigate(R.id.action_selectColorFragment_to_choiceConnectedDeviceFragment)
        }


        return binding.root

    }
}