package com.polytech.bmh.data.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.polytech.bmh.data.database.dao.ConnectedDeviceDao

import com.polytech.bmh.data.model.connecteddevice.ConnectedDeviceProperties

@Database(entities = [ConnectedDeviceProperties::class], version = 3, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract val connectedDevicesDao: ConnectedDeviceDao

    companion object
    {
        private var INSTANCE: com.polytech.bmh.data.database.Database? = null

        fun getInstance(context: Context): com.polytech.bmh.data.database.Database {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        com.polytech.bmh.data.database.Database::class.java,
                        "my_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}