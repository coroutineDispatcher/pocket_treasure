package com.stavro_xhardha.pockettreasure.ui.names

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.AssistedInject
import com.stavro_xhardha.pockettreasure.model.Name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NamesViewModel @AssistedInject constructor(
    private val repository: NamesRepository
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(): NamesViewModel
    }

    val allNamesList: MutableLiveData<List<Name>> = MutableLiveData()
    val progressBarVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorLayoutVisibility: MutableLiveData<Int> = MutableLiveData()

    init {
        loadNamesList()
    }

    private fun loadNamesList() {
        viewModelScope.launch(Dispatchers.IO) {
            startProgressBar()
            try {
                if (dataExistInDatabase()) {
                    makeNamesDatabaseCall()
                } else {
                    makeNamesApiCall()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showError()
            }
        }
    }

    private suspend fun startProgressBar() {
        withContext(Dispatchers.Main) {
            progressBarVisibility.value = View.VISIBLE
            errorLayoutVisibility.value = View.GONE
        }
    }

    private suspend fun makeNamesDatabaseCall() {
        val names = repository.getNamesFromDatabase()
        withContext(Dispatchers.Main) {
            allNamesList.value = names
            progressBarVisibility.value = View.GONE
            errorLayoutVisibility.value = View.GONE
        }
    }

    private suspend fun makeNamesApiCall() {
        val namesResponse = repository.fetchNintyNineNamesAsync()
        if (namesResponse.isSuccessful) {
            saveNameToDatabase(namesResponse.body()?.data)
            makeNamesDatabaseCall()
        } else {
            showError()
        }
    }

    private suspend fun saveNameToDatabase(data: ArrayList<Name>?) {
        data?.forEach {
            repository.saveToDatabase(it)
        }
    }

    private suspend fun dataExistInDatabase(): Boolean = repository.countNamesInDatabase() > 0

    private suspend fun showError() {
        withContext(Dispatchers.Main) {
            errorLayoutVisibility.value = View.VISIBLE
            progressBarVisibility.value = View.GONE
        }
    }

    fun retryConnection() {
        loadNamesList()
    }
}