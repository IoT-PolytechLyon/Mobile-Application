package com.polytech.bmh.data.model.connecteddevice

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
)

