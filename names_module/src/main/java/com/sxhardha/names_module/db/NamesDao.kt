package com.sxhardha.names_module.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sxhardha.names_module.model.Name

@Dao
interface NamesDao {

    @Query("SELECT * FROM names_of_creator")
    suspend fun selectAllNames(): List<Name>

    @Insert
    suspend fun insertName(name: Name)

    @Query("SELECT * FROM names_of_creator where id =:number")
    fun findName(number: Int): Name
}