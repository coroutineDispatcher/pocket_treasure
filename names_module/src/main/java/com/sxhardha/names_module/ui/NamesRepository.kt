package com.sxhardha.names_module.ui

import com.sxhardha.names_module.db.NamesDao
import com.sxhardha.names_module.model.Name
import com.sxhardha.names_module.model.NameResponse
import com.sxhardha.names_module.network.NamesApi
import retrofit2.Response
import javax.inject.Inject

class NamesRepository @Inject constructor(
    private val namesApi: NamesApi,
    private val namesDao: NamesDao
) {

    suspend fun fetchNintyNineNamesAsync(): Response<NameResponse> =
        namesApi.getNintyNineNamesAsync()

    suspend fun countNamesInDatabase(): Int = namesDao.selectAllNames().size

    suspend fun getNamesFromDatabase(): List<Name> = namesDao.selectAllNames()

    suspend fun saveToDatabase(name: Name) {
        namesDao.insertName(name)
    }
}