package com.sxhardha.names_module

import com.stavro_xhardha.core_module.model.Name
import com.stavro_xhardha.core_module.model.NameResponse
import com.stavro_xhardha.core_module.core_dependencies.TreasureApi
import com.stavro_xhardha.core_module.core_dependencies.NamesDao
import retrofit2.Response
import javax.inject.Inject

class NamesRepository @Inject constructor(private val treasureApi: TreasureApi, private val namesDao: NamesDao) {

    suspend fun fetchNintyNineNamesAsync(): Response<NameResponse> = treasureApi.getNintyNineNamesAsync()

    suspend fun countNamesInDatabase(): Int = namesDao.selectAllNames().size

    suspend fun getNamesFromDatabase(): List<Name> = namesDao.selectAllNames()

    suspend fun saveToDatabase(name: Name) {
        namesDao.insertName(name)
    }
}