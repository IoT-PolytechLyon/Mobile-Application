package com.polytech.bmh.data.model.connecteddevice

data class ConnectedDeviceData(
    val state: ConnectedDeviceState,
    val _id: String,
    val name: String,
    val description: String,
    val router: String
)
