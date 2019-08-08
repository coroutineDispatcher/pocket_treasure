package com.stavro_xhardha.pockettreasure.ui.quran

import com.stavro_xhardha.pockettreasure.brain.QURAN_API_CALL_BASE_URL
import com.stavro_xhardha.pockettreasure.model.Aya
import com.stavro_xhardha.pockettreasure.model.QuranResponse
import com.stavro_xhardha.pockettreasure.model.Surah
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.AyasDao
import com.stavro_xhardha.pockettreasure.room_db.SurahsDao
import retrofit2.Response
import javax.inject.Inject

class QuranRepository @Inject constructor(
    val treasureApi: TreasureApi,
    val surahsDao: SurahsDao,
    val ayasDao: AyasDao
) {

    suspend fun callTheQuranDataAsync(): Response<QuranResponse> =
        treasureApi.getQuranDataAsync(QURAN_API_CALL_BASE_URL)

    suspend fun findAllSurahs(): List<Surah> = surahsDao.getAllSuras()

    suspend fun findAllAyas(): List<Aya> = ayasDao.getAllAyas()

    suspend fun insertQuranReponseToDatabase(quranResponse: QuranResponse?) {
        quranResponse?.data?.surahs?.forEach { surah ->
            surahsDao.insertSurah(surah)
            surah.ayas.forEach { aya ->
                val ayaHelper = Aya(0, aya.audioUrl, aya.ayatText, aya.ayatNumber, aya.juz, surah.surahNumber)
                ayasDao.insertAya(ayaHelper)
            }
        }
    }
}