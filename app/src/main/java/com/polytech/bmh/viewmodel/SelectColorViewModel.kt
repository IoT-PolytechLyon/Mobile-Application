package com.polytech.bmh.viewmodel

import androidx.lifecycle.ViewModel
import com.polytech.bmh.repository.SelectColorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class SelectColorViewModel(private val selectColorRepository: SelectColorRepository) : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

}