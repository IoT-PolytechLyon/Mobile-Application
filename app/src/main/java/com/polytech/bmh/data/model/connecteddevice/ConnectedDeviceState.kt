package com.polytech.bmh.data.model.connecteddevice

data class ConnectedDeviceState(
    val pir_state: ConnectedDeviceStatePir,
    val nfc_state: ConnectedDeviceStateNfc,
    val led_state: ConnectedDeviceStateLed
)
