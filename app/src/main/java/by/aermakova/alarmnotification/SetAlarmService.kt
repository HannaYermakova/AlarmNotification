package by.aermakova.alarmnotification

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import by.aermakova.alarmnotification.util.Constants

class SetAlarmService : AlarmService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val time = getAlarmTime()
        if (time != null) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, createPending())
        }
        return START_NOT_STICKY
    }
}

class CancelAlarmService : AlarmService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmIntent = createPending()
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(alarmIntent)
        getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE).edit().clear().apply()
        return START_NOT_STICKY
    }
}