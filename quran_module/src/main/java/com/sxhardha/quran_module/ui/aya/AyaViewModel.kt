package com.sxhardha.quran_module.ui.aya

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.stavro_xhardha.core_module.brain.INITIAL_PAGE_SIZE
import com.sxhardha.quran_module.database.AyasDao
import com.sxhardha.quran_module.model.Aya
import javax.inject.Inject

class AyaViewModel @Inject constructor(
    private val ayasDao: AyasDao
) : ViewModel() {

    lateinit var ayas: LiveData<PagedList<Aya>>

    fun startSuraDataBaseCall(surahNumber: Int) {
        val listConfig = PagedList.Config.Builder()
            .setPageSize(INITIAL_PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()
        val dataSourceFactory = ayasDao.getAyasBySurahNumber(surahNumber)
        ayas = LivePagedListBuilder(dataSourceFactory, listConfig).build()
    }
}