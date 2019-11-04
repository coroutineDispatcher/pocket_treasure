package com.sxhardha.quran_module.ui.quran

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stavro_xhardha.core_module.core_dependencies.AppCoroutineDispatchers
import com.sxhardha.quran_module.model.Surah
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuranViewModel @Inject constructor(
    appCoroutineDispatchers: AppCoroutineDispatchers,
    private val repository: QuranRepository
) : ViewModel() {

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
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
        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher + coroutineExceptionHandler) {
            startQuranCall()
        }
    }

    private suspend fun startQuranCall() {
        showProgress()
        val surahsInDatabase = repository.getAllSurahs()
        val ayasInDatabase = repository.getAllAyas()

        if (surahsInDatabase.isEmpty() || ayasInDatabase.isEmpty() || surahsInDatabase.size != 114) {
            repository.deleteAllSurahs()
            repository.deleteAllAyas()
            val quranApiCall = repository.getQuranDataFromNetwork()
            if (quranApiCall.isSuccessful) {
                repository.insertData(quranApiCall.body())
                makeLocalDatabaseCall()
            } else {
                showError()
            }
        } else {
            makeLocalDatabaseCall()
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

    private fun showData() {
        _progressVisibility.postValue(View.GONE)
        _errorVisibility.postValue(View.GONE)
        _listVisibility.postValue(View.VISIBLE)
    }

    private suspend fun makeLocalDatabaseCall() {
        val surahs = repository.getAllSurahs()
        _surahs.postValue(surahs)
        showData()
    }

    fun remakeQuranCall() {
        viewModelScope.launch {
            startQuranCall()
        }
    }
}
