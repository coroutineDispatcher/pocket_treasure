package com.stavro_xhardha.pockettreasure.background

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.PrayerTiming
import com.stavro_xhardha.pockettreasure.room_db.PrayerTimesDao
import com.stavro_xhardha.rocket.Rocket
import org.joda.time.DateTime
import javax.inject.Inject

class OfflinePrayerScheduler @Inject constructor(
    val rocket: Rocket,
    val context: Application,
    val prayerstimeDao: PrayerTimesDao
) {

    suspend fun initScheduler() {
        val calendarHelper = DateTime().withTime(9, 1, 1, 0)
        val todaysSelection = prayerstimeDao.selectAllPrayersWhereDateAndIsFired(calendarHelper.millis, 0)
        if (todaysSelection != null) {
            scheduleNotificationTimes(todaysSelection)
            invokeTomorrowAlarm()
        } else {
            val currentTime = DateTime()
            if (currentTime.dayOfMonth != 1 && currentTime.monthOfYear != 1) {
                prayerstimeDao.deleteAllDataInside()
                rocket.writeBoolean(WORKER_FIRED_KEY, false)
                throw RuntimeException(" Crash, Boom , Pew Pew, 1st January yet to come or has passed :) ")
            } else {
                startPrayerTimesWorkManager(context)
            }
        }
    }

    private suspend fun scheduleNotificationTimes(it: PrayerTiming) {
        scheduleAlarmForPrayer(
            rocket.readBoolean(NOTIFY_USER_FOR_FAJR),
            it.fajr,
            PENDING_INTENT_FIRE_NOTIFICATION_FAJR
        )
        scheduleAlarmForPrayer(
            rocket.readBoolean(NOTIFY_USER_FOR_DHUHR),
            it.dhuhr,
            PENDING_INTENT_FIRE_NOTIFICATION_DHUHR
        )
        scheduleAlarmForPrayer(
            rocket.readBoolean(NOTIFY_USER_FOR_ASR),
            it.asr,
            PENDING_INTENT_FIRE_NOTIFICATION_ASR
        )
        scheduleAlarmForPrayer(
            rocket.readBoolean(NOTIFY_USER_FOR_MAGHRIB),
            it.magrib,
            PENDING_INTENT_FIRE_NOTIFICATION_MAGHRIB
        )
        scheduleAlarmForPrayer(
            rocket.readBoolean(NOTIFY_USER_FOR_ISHA),
            it.isha,
            PENDING_INTENT_FIRE_NOTIFICATION_ISHA
        )
    }

    private fun invokeTomorrowAlarm() {
        val midnight = DateTime().plusDays(1).withTime(0, 1, 1, 0)

        schedulePrayingAlarm(
            context,
            midnight,
            PENDING_INTENT_SYNC,
            MidnightScheduler::class.java
        )

        if (isDebugMode)
            Log.d(
                APPLICATION_TAG,
                " ALARM SET ALSO IN: DAY: ${midnight.dayOfMonth}, HR: ${midnight.hourOfDay}, " +
                        "Minute: ${midnight.minuteOfHour} and Second: ${midnight.secondOfMinute} "
            )
    }

    private suspend fun scheduleAlarmForPrayer(
        isNotificationTriguable: Boolean,
        prayerTime: String,
        pendingIntentKey: Int
    ) {
        val currentTime = DateTime()
        if (isNotificationTriguable) {
            if (currentTime.isBefore(getCurrentDayPrayerImplementation(prayerTime))) {
                schedulePrayingAlarm(
                    context,
                    getCurrentDayPrayerImplementation(prayerTime),
                    pendingIntentKey,
                    PrayerTimeNotificationReceiver::class.java
                )
                if (isDebugMode)
                    Log.d(APPLICATION_TAG, "ALARM SET IN: $prayerTime")
            }
        }

        val dataToUpdate = getCurrentDayPrayerImplementation(prayerTime).millis
        prayerstimeDao.updatePrayersWhehereDate(dataToUpdate, 1)
    }

    private fun schedulePrayingAlarm(mContext: Context, time: DateTime, pendingIntentKey: Int, desiredClass: Class<*>) {
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

    private fun getCurrentDayPrayerImplementation(prayerTime: String): DateTime =
        if (prayerTime.isNotEmpty()) {
            val actualHour =
                if (prayerTime.startsWith("0")) prayerTime.substring(1, 2).toInt() else prayerTime.substring(
                    0,
                    2
                ).toInt()
            val actualminute = if (prayerTime.substring(3, 5).startsWith("0")) prayerTime.substring(
                4,
                5
            ).toInt() else prayerTime.substring(3, 5).toInt()
            DateTime().withTime(actualHour, actualminute, 0, 0)
        } else DateTime()
}