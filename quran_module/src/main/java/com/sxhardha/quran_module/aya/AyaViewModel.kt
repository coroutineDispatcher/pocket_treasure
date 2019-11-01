package com.sxhardha.quran_module.aya

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.stavro_xhardha.core_module.brain.buildPagedList
import com.stavro_xhardha.core_module.core_dependencies.AyasDao
import com.stavro_xhardha.core_module.model.Aya
import javax.inject.Inject

class AyaViewModel @Inject constructor(
    private val ayasDao: AyasDao
) : ViewModel() {

    lateinit var ayas: LiveData<PagedList<Aya>>

    fun startSuraDataBaseCall(surahNumber: Int) {
        val listConfig = buildPagedList()
        val dataSourceFactory = ayasDao.getAyasBySurahNumber(surahNumber)
        ayas = LivePagedListBuilder(dataSourceFactory, listConfig).build()
    }
}