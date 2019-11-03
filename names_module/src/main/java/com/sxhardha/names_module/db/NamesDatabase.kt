package com.sxhardha.names_module.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sxhardha.names_module.model.Name

@Database(
    version = 1,
    entities = [Name::class]
)
abstract class NamesDatabase : RoomDatabase() {
    abstract fun namesDao(): NamesDao
}