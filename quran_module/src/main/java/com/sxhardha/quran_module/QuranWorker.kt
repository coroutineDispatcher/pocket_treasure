package com.sxhardha.quran_module

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.stavro_xhardha.core_module.brain.QURAN_API_CALL_BASE_URL
import com.stavro_xhardha.core_module.model.Aya
import com.stavro_xhardha.core_module.model.QuranResponse
import com.stavro_xhardha.core_module.core_dependencies.TreasureApi
import com.stavro_xhardha.core_module.core_dependencies.AyasDao
import com.stavro_xhardha.core_module.core_dependencies.SurahsDao
import com.stavro_xhardha.core_module.dependency_injection.CoreApplication
import kotlinx.coroutines.*


class QuranWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    private lateinit var treasureApi: TreasureApi
    private lateinit var surahsDao: SurahsDao
    private lateinit var ayasDao: AyasDao
    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Result.failure()
        }

    override suspend fun doWork(): Result = coroutineScope {
        initDependencies()

        val job = launch(Dispatchers.IO + coroutineExceptionHandler) {
            val surahsInDatabase = surahsDao.getAllSuras()
            val ayasInDatabase = ayasDao.getAllAyas()

            if (surahsInDatabase.isEmpty() || ayasInDatabase.isEmpty() || surahsInDatabase.size != 114) {
                surahsDao.deleteAllSurahs()
                ayasDao.deleteAllAyas()
                val quranApiCall = treasureApi.getQuranDataAsync(QURAN_API_CALL_BASE_URL)
                if (quranApiCall.isSuccessful) {
                    insertDataToDatabase(quranApiCall.body())
                    withContext(Dispatchers.Main) {
                        Result.success()
                    }
                } else {
                    Result.failure()
                }
            } else {
                Result.success()
            }
        }

        job.join()
        Result.success()
    }

    private suspend fun insertDataToDatabase(body: QuranResponse?) {
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

    private fun initDependencies() {
        treasureApi = CoreApplication.getCoreComponent().treasureApi
        surahsDao =
            CoreApplication.getCoreComponent().treasureDatabase.surahsDao()
        ayasDao =
            CoreApplication.getCoreComponent().treasureDatabase.ayasDao()
    }
}