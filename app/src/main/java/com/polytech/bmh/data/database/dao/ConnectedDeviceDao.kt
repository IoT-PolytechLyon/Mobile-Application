package com.polytech.bmh.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.polytech.bmh.data.model.ConnectedDeviceProperties

@Dao
interface ConnectedDeviceDao {
    @Insert
    fun insert(connectedDevice: ConnectedDeviceProperties): Long

    @Query("SELECT * from connected_devices")
    fun get() : List<ConnectedDeviceProperties>
}