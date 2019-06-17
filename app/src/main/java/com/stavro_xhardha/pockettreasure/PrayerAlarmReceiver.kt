package com.stavro_xhardha.pockettreasure

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.rocket.Rocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PrayerAlarmReceiver : BroadcastReceiver() {

    private lateinit var treasureApi: TreasureApi
    private lateinit var rocket: Rocket
    private lateinit var mContext: Context

    override fun onReceive(context: Context?, intent: Intent?) {
        mContext = context!!
        giveValuesToDependencies()
        //it's midnight
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val city = rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)
                val country = rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)
                val prayerTimesResponse = treasureApi.getPrayerTimesTodayAsync(
                    city,
                    country, 1
                )
                if (prayerTimesResponse.isSuccessful) {
                    setPrayerAlarms(prayerTimesResponse.body())
                } else {
                    tryInOneHour()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                tryInOneHour()
            }
        }
    }

    private fun tryInOneHour() {
        val intent = Intent(mContext, PrayerAlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(mContext, PENDING_INTENT_SYNC, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(
            AlarmManager.RTC,
            System.currentTimeMillis() + ((60 * 1000) * 60),
            pendingIntent
        )
    }

    private fun giveValuesToDependencies() {
        val appComponent = PocketTreasureApplication.getPocketTreasureComponent()
        treasureApi = appComponent.getTreasureApi()
        rocket = appComponent.getSharedPreferences()
    }

    private fun setPrayerAlarms(prayerTimeResponse: PrayerTimeResponse?) {
        prayerTimeResponse.let {
            if (rocket.readBoolean(NOTIFY_USER_FOR_FAJR)) {
                setAlarm(prayerTimeResponse?.data?.timings?.fajr!!, PENDING_INTENT_FIRE_NOTIFICATION_FAJR)
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_DHUHR)) {
                setAlarm(prayerTimeResponse?.data?.timings?.dhuhr!!, PENDING_INTENT_FIRE_NOTIFICATION_DHUHR)
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_ASR)) {
                setAlarm(prayerTimeResponse?.data?.timings?.asr!!, PENDING_INTENT_FIRE_NOTIFICATION_ASR)
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_MAGHRIB)) {
                setAlarm(prayerTimeResponse?.data?.timings?.magrib!!, PENDING_INTENT_FIRE_NOTIFICATION_MAGHRIB)
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_ISHA)) {
                setAlarm(prayerTimeResponse?.data?.timings?.isha!!, PENDING_INTENT_FIRE_NOTIFICATION_ISHA)
            }

            invokeTomorowAlarm(prayerTimeResponse!!.data.timings.midnight)
        }
    }

    private fun invokeTomorowAlarm(midnight: String) {
        val midnightTime = getMidnightImplementation(midnight)

        val intent = Intent(mContext, PrayerAlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(mContext, PENDING_INTENT_SYNC, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(
            AlarmManager.RTC,
            midnightTime.timeInMillis,
            pendingIntent
        )
    }

    private fun setAlarm(prayerTime: String, pendingIntentKey: Int) {
        val prayerTimeCalendar = getCurrentDayPrayerImplementation(prayerTime)

        val intent = Intent(mContext, PrayerTimeAlarm::class.java)

        val pendingIntent =
            PendingIntent.getBroadcast(
                mContext,
                pendingIntentKey,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        scheduleAlarm(prayerTimeCalendar, alarmManager, pendingIntent)
    }
}