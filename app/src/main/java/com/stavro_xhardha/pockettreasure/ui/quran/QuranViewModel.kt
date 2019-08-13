package com.stavro_xhardha.pockettreasure.ui.quran

import android.view.View
import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.stavro_xhardha.pockettreasure.model.QuranResponse
import com.stavro_xhardha.pockettreasure.model.Surah
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuranViewModel @AssistedInject constructor(
    private val repository: QuranRepository,
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): QuranViewModel
    }

    private val _surahs: MutableLiveData<List<Surah>> = MutableLiveData()
    private val _errorVisibility: MutableLiveData<Int> = MutableLiveData()
    private val _listVisibility: MutableLiveData<Int> = MutableLiveData()
    private val _progressVisibility: MutableLiveData<Int> = MutableLiveData()

    val surahs: LiveData<List<Surah>> = _surahs
    val errorVisibility: LiveData<Int> = _errorVisibility
    val listVisibility: LiveData<Int> = _listVisibility
    val progressVisibility: LiveData<Int> = _progressVisibility

    init {
        startQuranImplementation()
    }

    fun startQuranImplementation() {
        viewModelScope.launch(Dispatchers.IO) {
            showProgress()
            val surahsInDatabase = repository.findAllSurahs()
            val ayasInDatabase = repository.findAllAyas()
            if (surahsInDatabase.isEmpty() && ayasInDatabase.isEmpty()) {
                try {
                    val quranApiCall = repository.callTheQuranDataAsync()
                    if (quranApiCall.isSuccessful) {
                        insertDataToDatabase(quranApiCall.body())
                        makeLocalDatabaseCall()
                    } else {
                        showError()
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    showError()
                }
            } else {
                makeLocalDatabaseCall()
            }
        }
    }

    private suspend fun showProgress() {
        withContext(Dispatchers.Main) {
            _progressVisibility.value = View.VISIBLE
            _errorVisibility.value = View.GONE
            _listVisibility.value = View.GONE
        }
    }

    private suspend fun showError() {
        withContext(Dispatchers.Main) {
            _progressVisibility.value = View.GONE
            _errorVisibility.value = View.VISIBLE
            _listVisibility.value = View.GONE
        }
    }

    private fun resetVisibilityValues() {
        _progressVisibility.value = View.GONE
        _errorVisibility.value = View.GONE
        _listVisibility.value = View.VISIBLE
    }

    private suspend fun insertDataToDatabase(quranApiCall: QuranResponse?) {
        repository.insertQuranReponseToDatabase(quranApiCall)
    }

    private suspend fun makeLocalDatabaseCall() {
        val surahs = repository.findAllSurahs()
        withContext(Dispatchers.Main) {
            _surahs.value = surahs
            resetVisibilityValues()
        }
    }
}
