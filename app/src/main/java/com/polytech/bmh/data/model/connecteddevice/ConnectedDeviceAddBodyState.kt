package com.polytech.bmh.data.model.connecteddevice

data class ConnectedDeviceAddBodyState(
    val nameError: String? = null,
    val descriptionError: String? = null,
    val routerError: String? = null,
    val isDataValid: Boolean = false
)