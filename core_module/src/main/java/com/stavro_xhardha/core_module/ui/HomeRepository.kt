package com.stavro_xhardha.core_module.ui

import com.stavro_xhardha.core_module.R
import com.stavro_xhardha.core_module.brain.*
import com.stavro_xhardha.core_module.core_dependencies.TreasureApi
import com.stavro_xhardha.core_module.model.HomePrayerTime
import com.stavro_xhardha.core_module.model.PrayerTimeResponse
import com.stavro_xhardha.rocket.Rocket
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val treasureApi: TreasureApi,
    private val mSharedPreferences: Rocket
) {
    suspend fun makePrayerCallAsync(): Response<PrayerTimeResponse> =
        treasureApi.getPrayerTimesTodayAsync(
            mSharedPreferences.readString(CAPITAL_SHARED_PREFERENCES_KEY),
            mSharedPreferences.readString(COUNTRY_SHARED_PREFERENCE_KEY)
        )

    fun saveFajrTime(fajr: String) {
        if (fajr.isNotEmpty())
            mSharedPreferences.writeString(FAJR_KEY, fajr)
    }

    fun saveDhuhrTime(dhuhr: String) {
        if (dhuhr.isNotEmpty())
            mSharedPreferences.writeString(DHUHR_KEY, dhuhr)
    }

    fun saveAsrTime(asr: String) {
        if (asr.isNotEmpty())
            mSharedPreferences.writeString(ASR_KEY, asr)
    }

    fun saveMagribTime(magrib: String) {
        if (magrib.isNotEmpty())
            mSharedPreferences.writeString(MAGHRIB_KEY, magrib)
    }

    fun saveIshaTime(isha: String) {
        if (isha.isNotEmpty())
            mSharedPreferences.writeString(ISHA_KEY, isha)
    }

    fun saveDayOfMonth(datyOfMonth: Int) {
        mSharedPreferences.writeInt(GREGORIAN_DAY_KEY, datyOfMonth)
    }

    fun saveYear(year: Int) {
        if (year > 0)
            mSharedPreferences.writeInt(GREGORIAN_YEAR_KEY, year)
    }

    fun saveMonthOfYear(month: Int) {
        if (month >= 0)
            mSharedPreferences.writeInt(GREGORIAN_MONTH_KEY, month)
    }

    fun saveMonthName(monthNameInEnglish: String) {
        if (monthNameInEnglish.isNotEmpty())
            mSharedPreferences.writeString(GREGORIAN_MONTH_NAME_KEY, monthNameInEnglish)
    }

    fun saveDayOfMonthHijri(day: String) {
        if (day.isNotEmpty())
            mSharedPreferences.writeString(HIRJI_DAY_OF_MONTH_KEY, day)
    }

    fun saveMonthOfYearHijri(monthNameHijri: String) {
        if (monthNameHijri.isNotEmpty())
            mSharedPreferences.writeString(HIJRI_MONTH_NAME_KEY, monthNameHijri)
    }

    fun saveYearHijri(year: String) {
        if (year.isNotEmpty())
            mSharedPreferences.writeString(HIJRI_YEAR_KEY, year)
    }

    fun saveMidnight(midnight: String) {
        if (midnight.isNotEmpty())
            mSharedPreferences.writeString(MIDNIGHT_KEY, midnight)
    }

    fun saveFinishFajrTime(sunrise: String) {
        mSharedPreferences.writeString(SUNRISE_KEY, sunrise)
    }

    private fun readFejrtime(): String? = mSharedPreferences.readString(FAJR_KEY)

    private fun readDhuhrTime(): String? = mSharedPreferences.readString(DHUHR_KEY)

    private fun readAsrTime(): String? = mSharedPreferences.readString(ASR_KEY)

    private fun readMaghribTime(): String? = mSharedPreferences.readString(MAGHRIB_KEY)

    private fun readIshaTime(): String? = mSharedPreferences.readString(ISHA_KEY)

    private fun readFinishFajrTime(): String? = mSharedPreferences.readString(SUNRISE_KEY)

    fun getCurrentRegisteredDay(): Int = mSharedPreferences.readInt(GREGORIAN_DAY_KEY)

    fun getCurrentRegisteredMonth(): Int = mSharedPreferences.readInt(GREGORIAN_MONTH_KEY)

    fun getCurrentRegisteredYear(): Int = mSharedPreferences.readInt(GREGORIAN_YEAR_KEY)

    fun countryHasBeenUpdated(): Boolean = mSharedPreferences.readBoolean(COUNTRY_UPDATED)

    fun updateCountryState() {
        mSharedPreferences.writeBoolean(COUNTRY_UPDATED, false)
    }

    fun getHomeData(): ArrayList<HomePrayerTime> {
        return arrayListOf(
            HomePrayerTime(
                "Fajr",
                "${readFejrtime()} - ${readFinishFajrTime()}",
                R.color.card_view_default,
                R.drawable.ic_fajr_colorful_sun
            ), HomePrayerTime(
                "Dhuhr",
                readDhuhrTime() ?: "",
                R.color.card_view_default,
                R.drawable.ic_dhuhr_colorful_sun
            ), HomePrayerTime(
                "Asr",
                readAsrTime() ?: "",
                R.color.card_view_default,
                R.drawable.ic_asr_colorful_sun
            ), HomePrayerTime(
                "Maghrib",
                readMaghribTime() ?: "",
                R.color.card_view_default,
                R.drawable.ic_maghrib_colorful_sun
            ), HomePrayerTime(
                "Isha",
                readIshaTime() ?: "",
                R.color.card_view_default,
                R.drawable.ic_isha_moon
            )
        )
    }
}