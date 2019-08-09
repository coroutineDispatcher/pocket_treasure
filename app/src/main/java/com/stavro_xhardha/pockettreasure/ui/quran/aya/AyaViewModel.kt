package com.stavro_xhardha.pockettreasure.ui.quran.aya

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.squareup.inject.assisted.AssistedInject
import com.stavro_xhardha.pockettreasure.brain.buildPagedList
import com.stavro_xhardha.pockettreasure.model.Aya
import com.stavro_xhardha.pockettreasure.room_db.AyasDao

class AyaViewModel @AssistedInject constructor(private val ayasDao: AyasDao) : ViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(): AyaViewModel
    }

    lateinit var ayas: LiveData<PagedList<Aya>>

    fun startSuraDataBaseCall(surahNumber: Int) {
        val listConfig = buildPagedList()
        val dataSourceFactory = ayasDao.getAyasBySurahNumber(surahNumber)
        ayas = LivePagedListBuilder(dataSourceFactory, listConfig).build()
    }
}