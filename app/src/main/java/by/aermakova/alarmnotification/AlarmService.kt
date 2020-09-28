package by.aermakova.alarmnotification

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import by.aermakova.alarmnotification.data.Time
import by.aermakova.alarmnotification.util.Constants
import com.google.gson.Gson
import java.util.*
import java.util.jar.Manifest

abstract class AlarmService : Service() {

    protected fun getAlarmTime(): Long? {
        val timeText =
            getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE).getString(
                Constants.ALARM_TIME_PREF, null)
        if (timeText != null) {
            val time = Gson().fromJson(timeText, Time::class.java)
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, time.hours)
                set(Calendar.MINUTE, time.minutes)
                set(Calendar.SECOND, 0)
            }
            Log.d("A_SetAlarm", "getAlarmTime ${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(
                Calendar.MINUTE)}")
            return calendar.timeInMillis
        }
        return null
    }

    protected fun createPending(): PendingIntent {
        val intent = Intent(applicationContext, AlarmReceiver::class.java).apply {
            action = Constants.ACTION_ALARM
        }
        return PendingIntent.getBroadcast(
            applicationContext,
            1,
            Intent(intent),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}