package by.aermakova.alarmnotification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.format.DateFormat
import by.aermakova.alarmnotification.data.Time
import by.aermakova.alarmnotification.ui.UpdateTimer
import by.aermakova.alarmnotification.util.Constants.ACTION_SLEEP
import by.aermakova.alarmnotification.util.Constants.ALARM_TIME_PREF
import by.aermakova.alarmnotification.util.Constants.NOTIFICATION_ID
import by.aermakova.alarmnotification.util.Constants.PREFERENCES
import com.google.gson.Gson
import java.util.*

class SleepReceiver : BroadcastReceiver() {

    companion object {
        private const val SLEEP_TIME = 5
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            if (ACTION_SLEEP == intent.action) {
                val calendar = Calendar.getInstance().apply {
                    time = Date()
                    add(Calendar.MINUTE, SLEEP_TIME)
                }
                val hour = calendar.get(Calendar.HOUR)
                val minute = calendar.get(Calendar.MINUTE)
                val time = Time(hour, minute, DateFormat.is24HourFormat(context))
                val timeText = Gson().toJson(time)
                context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit()
                    .putString(ALARM_TIME_PREF, timeText).apply()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(Intent(context, SetAlarmService::class.java))
                } else context.startService(Intent(context, SetAlarmService::class.java))

                UpdateTimer.updateTimer(context, timeText)
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(NOTIFICATION_ID)
        }
    }
}