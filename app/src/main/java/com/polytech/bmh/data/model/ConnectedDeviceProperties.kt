package com.polytech.bmh.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.polytech.bmh.BR

@Keep
@Entity(tableName = "connected_devices")
data class ConnectedDeviceProperties(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "generatedId")
    var generatedId: Long = 0L,

    @NonNull
    @ColumnInfo(name = "_id")
    var _id: String? = "",
    @ColumnInfo(name = "name")
    var name: String? = "",
    @ColumnInfo(name = "description")
    var description: String? = "",
    @ColumnInfo(name = "router")
    var router: String? = ""
) : Parcelable, BaseObservable() {




    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

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
