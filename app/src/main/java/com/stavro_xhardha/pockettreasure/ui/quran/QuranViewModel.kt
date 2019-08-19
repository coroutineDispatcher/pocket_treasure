package com.stavro_xhardha.pockettreasure.ui.quran

import android.view.View
import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.stavro_xhardha.pockettreasure.model.QuranResponse
import com.stavro_xhardha.pockettreasure.model.Surah
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuranViewModel @AssistedInject constructor(
    private val repository: QuranRepository,
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): QuranViewModel
    }

    private val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        showError()
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
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            showProgress()
            val surahsInDatabase = repository.findAllSurahs()
            val ayasInDatabase = repository.findAllAyas()
            if (surahsInDatabase.isEmpty() && ayasInDatabase.isEmpty()) {
                val quranApiCall = repository.callTheQuranDataAsync()
                if (quranApiCall.isSuccessful) {
                    insertDataToDatabase(quranApiCall.body())
                    makeLocalDatabaseCall()
                } else {
                    showError()
                }
            } else {
                makeLocalDatabaseCall()
            }
        }
    }

    private fun showProgress() {
        _progressVisibility.postValue(View.VISIBLE)
        _errorVisibility.postValue(View.GONE)
        _listVisibility.postValue(View.GONE)
    }

    private fun showError() {
        _progressVisibility.postValue(View.GONE)
        _errorVisibility.postValue(View.VISIBLE)
        _listVisibility.postValue(View.GONE)
    }

    private fun resetVisibilityValues() {
        _progressVisibility.postValue(View.GONE)
        _errorVisibility.postValue(View.GONE)
        _listVisibility.postValue(View.VISIBLE)
    }

    private suspend fun insertDataToDatabase(quranApiCall: QuranResponse?) {
        repository.insertQuranReponseToDatabase(quranApiCall)
    }

    private suspend fun makeLocalDatabaseCall() {
        val surahs = repository.findAllSurahs()
        _surahs.postValue(surahs)
        resetVisibilityValues()
    }
}
