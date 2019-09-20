package com.stavro_xhardha.pockettreasure.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.PrayerMonthDays
import com.stavro_xhardha.pockettreasure.model.PrayerTimeYearResponse
import com.stavro_xhardha.pockettreasure.model.PrayerTiming
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.PrayerTimesDao
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import com.stavro_xhardha.rocket.Rocket
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime

class PrayerTimeWorkManager(val context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    private lateinit var treasureDatabase: TreasureDatabase
    private lateinit var treasureApi: TreasureApi
    private lateinit var rocket: Rocket
    private lateinit var prayerTimesDao: PrayerTimesDao
    private lateinit var offlineScheduler: OfflinePrayerScheduler

    override suspend fun doWork(): Result = coroutineScope {
        instantiateDependencies()
        launch {
            val fajrTime = rocket.readString(FAJR_KEY)
            val dhuhrTime = rocket.readString(DHUHR_KEY)
            val asrTime = rocket.readString(ASR_KEY)
            val maghribTime = rocket.readString(MAGHRIB_KEY)
            val ishaTime = rocket.readString(ISHA_KEY)

            val currentTIme = LocalTime()

            if (currentTIme.isBefore(localTime(fajrTime!!))) {
                scheduleAlarmForPrayer(
                    rocket.readBoolean(NOTIFY_USER_FOR_FAJR),
                    fajrTime,
                    PENDING_INTENT_FIRE_NOTIFICATION_FAJR
                )
            }
            if (currentTIme.isBefore(localTime(dhuhrTime!!))) {
                scheduleAlarmForPrayer(
                    rocket.readBoolean(NOTIFY_USER_FOR_DHUHR),
                    dhuhrTime,
                    PENDING_INTENT_FIRE_NOTIFICATION_DHUHR
                )
            }
            if (currentTIme.isBefore(localTime(asrTime!!))) {
                scheduleAlarmForPrayer(
                    rocket.readBoolean(NOTIFY_USER_FOR_ASR),
                    asrTime,
                    PENDING_INTENT_FIRE_NOTIFICATION_ASR
                )
            }
            if (currentTIme.isBefore(localTime(maghribTime!!))) {
                scheduleAlarmForPrayer(
                    rocket.readBoolean(NOTIFY_USER_FOR_MAGHRIB),
                    maghribTime,
                    PENDING_INTENT_FIRE_NOTIFICATION_MAGHRIB
                )
            }
            if (currentTIme.isBefore(localTime(ishaTime!!))) {
                scheduleAlarmForPrayer(
                    rocket.readBoolean(NOTIFY_USER_FOR_ISHA),
                    ishaTime,
                    PENDING_INTENT_FIRE_NOTIFICATION_ISHA
                )
            }
        }
        Result.success()
    }

    private fun scheduleAlarmForPrayer(
        isNotificationTriguable: Boolean,
        prayerTime: String,
        pendingIntentKey: Int
    ) {
        if (isNotificationTriguable) {
            schedulePrayingAlarm(
                context,
                getCurrentDayPrayerImplementation(prayerTime),
                pendingIntentKey,
                PrayerTimeNotificationReceiver::class.java
            )
        }
    }

    private fun checkIntentVariables(intentKey: Int, intent: Intent) {
        when (intentKey) {
            PENDING_INTENT_FIRE_NOTIFICATION_FAJR -> {
                intent.putExtra(PRAYER_TITLE, FAJR)
                intent.putExtra(PRAYER_DESCRIPTION, "Fajr time has arrived")
            }
            PENDING_INTENT_FIRE_NOTIFICATION_DHUHR -> {
                intent.putExtra(PRAYER_TITLE, DHUHR)
                intent.putExtra(PRAYER_DESCRIPTION, "Dhuhr time has arrived")
            }
            PENDING_INTENT_FIRE_NOTIFICATION_ASR -> {
                intent.putExtra(PRAYER_TITLE, ASR)
                intent.putExtra(PRAYER_DESCRIPTION, "Asr time has arrived")
            }
            PENDING_INTENT_FIRE_NOTIFICATION_MAGHRIB -> {
                intent.putExtra(PRAYER_TITLE, MAGHRIB)
                intent.putExtra(PRAYER_DESCRIPTION, "Maghrib time has arrived")
            }
            PENDING_INTENT_FIRE_NOTIFICATION_ISHA -> {
                intent.putExtra(PRAYER_TITLE, ISHA)
                intent.putExtra(PRAYER_DESCRIPTION, "Isha time has arrived")
            }
        }
    }

    private fun schedulePrayingAlarm(
        mContext: Context,
        time: DateTime,
        pendingIntentKey: Int,
        desiredClass: Class<*>
    ) {
        val intent = Intent(mContext, desiredClass)
        checkIntentVariables(pendingIntentKey, intent)
        val pendingIntent =
            PendingIntent.getBroadcast(
                mContext,
                pendingIntentKey,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(AlarmManager.RTC, time.millis, pendingIntent)

        if (isDebugMode)
            Log.d(APPLICATION_TAG, "Alarm set at ${time.millis}")
    }

    private fun getCurrentDayPrayerImplementation(prayerTime: String): DateTime =
        if (prayerTime.isNotEmpty()) {
            val actualHour =
                if (prayerTime.startsWith("0")) prayerTime.substring(
                    1,
                    2
                ).toInt() else prayerTime.substring(
                    0,
                    2
                ).toInt()
            val actualminute = if (prayerTime.substring(3, 5).startsWith("0")) prayerTime.substring(
                4,
                5
            ).toInt() else prayerTime.substring(3, 5).toInt()
            DateTime().withTime(actualHour, actualminute, 0, 0)
        } else DateTime()

    private fun localTime(timeOfPrayer: String): LocalTime = LocalTime(
        (timeOfPrayer.substring(0, 2).toInt()),
        (timeOfPrayer.substring(3, 5)).toInt()
    )

    private suspend fun handleResponse(body: PrayerTimeYearResponse?) {
        reviewCurrentMonth(
            body?.data?.january!!,
            body.data.february,
            body.data.march,
            body.data.april,
            body.data.may,
            body.data.june,
            body.data.july,
            body.data.august,
            body.data.september,
            body.data.october,
            body.data.november,
            body.data.december
        )

        if (isDebugMode) {
            val selection = prayerTimesDao.selectAll()
            selection.forEach {
                Log.d(
                    APPLICATION_TAG,
                    " DATA INSERTED : ${it.id} and size of selection is: ${selection.size}"
                )
            }
        }

        offlineScheduler.initScheduler()
        rocket.writeBoolean(DATA_ARE_READY, true)
    }

    private suspend fun reviewCurrentMonth(vararg monthDays: List<PrayerMonthDays>) {
        val currentDateTime = DateTime()
        monthDays.forEach {
            it.forEach { prayerMonthDays ->
                val incomingTime = DateTime(prayerMonthDays.prayerDate.timestamp.toLong() * 1000L)
                if (incomingTime.isAfter(currentDateTime) || (incomingTime.toLocalDate()).equals(
                        LocalDate()
                    )
                ) {
                    prayerTimesDao.insertPrayerTimes(
                        PrayerTiming(
                            id = 0,
                            fajr = prayerMonthDays.timing.fajr,
                            sunrise = prayerMonthDays.timing.sunrise,
                            dhuhr = prayerMonthDays.timing.dhuhr,
                            asr = prayerMonthDays.timing.asr,
                            sunset = prayerMonthDays.timing.sunset,
                            magrib = prayerMonthDays.timing.magrib,
                            isha = prayerMonthDays.timing.isha,
                            midnight = prayerMonthDays.timing.midnight,
                            imsak = prayerMonthDays.timing.imsak,
                            isFired = 0,
                            timestamp = prayerMonthDays.prayerDate.timestamp.toLong() * 1000L
                        )
                    )
                }
            }
        }
    }

    private fun instantiateDependencies() {
        val application = PocketTreasureApplication.getPocketTreasureComponent()
        treasureApi = application.treasureApi
        rocket = application.getSharedPreferences
        treasureDatabase = application.treasureDatabase
        prayerTimesDao = treasureDatabase.prayerTimesDao()
        offlineScheduler = application.offlineScheduler
    }
}