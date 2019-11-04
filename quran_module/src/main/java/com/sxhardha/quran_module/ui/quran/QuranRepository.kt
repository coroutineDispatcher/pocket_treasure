package com.sxhardha.quran_module.ui.quran

import com.stavro_xhardha.core_module.brain.QURAN_API_CALL_BASE_URL
import com.sxhardha.quran_module.database.AyasDao
import com.sxhardha.quran_module.database.SurahsDao
import com.sxhardha.quran_module.model.Aya
import com.sxhardha.quran_module.model.QuranResponse
import com.sxhardha.quran_module.model.Surah
import com.sxhardha.quran_module.network.QuranApi
import retrofit2.Response
import javax.inject.Inject


class QuranRepository @Inject constructor(
    private val surahsDao: SurahsDao,
    private val ayasDao: AyasDao,
    private val quranApi: QuranApi
) {

    suspend fun getAllSurahs(): List<Surah> = surahsDao.getAllSuras()

    suspend fun getAllAyas(): List<Aya> = ayasDao.getAllAyas()

    suspend fun deleteAllSurahs() {
        surahsDao.deleteAllSurahs()
    }

    suspend fun deleteAllAyas() {
        ayasDao.deleteAllAyas()
    }

    suspend fun insertData(body: QuranResponse?) {
        body?.data?.surahs?.forEach { surah ->
            surahsDao.insertSurah(surah)
            surah.ayas.forEach { aya ->
                val ayaHelper = Aya(
                    0,
                    aya.audioUrl,
                    aya.ayatText,
                    aya.ayatNumber,
                    aya.juz,
                    surah.surahNumber
                )
                ayasDao.insertAya(ayaHelper)
            }
        }
    }

    suspend fun getQuranDataFromNetwork(): Response<QuranResponse> =
        quranApi.getQuranDataAsync(QURAN_API_CALL_BASE_URL)
}