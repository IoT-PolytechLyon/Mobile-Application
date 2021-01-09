package com.polytech.bmh.data.model

import android.os.Parcel
import android.os.Parcelable

data class ConnectedDeviceProperties(
    val _id: String?,
    val name: String?,
    val description: String?,
    val router: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(router)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ConnectedDeviceProperties> {
        override fun createFromParcel(parcel: Parcel): ConnectedDeviceProperties {
            return ConnectedDeviceProperties(parcel)
        }

        override fun newArray(size: Int): Array<ConnectedDeviceProperties?> {
            return arrayOfNulls(size)
        }
    }
}
