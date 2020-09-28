package by.aermakova.alarmnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import by.aermakova.alarmnotification.util.Constants.ACTION_ALARM
import by.aermakova.alarmnotification.util.Constants.ACTION_SLEEP
import by.aermakova.alarmnotification.util.Constants.NOTIFICATION_ID
import by.aermakova.alarmnotification.util.Constants.PREFERENCES
import by.aermakova.alarmnotification.ui.UpdateTimer

class AlarmTrigger : JobIntentService() {

    companion object {
        private const val JOB_ID = 1234

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, AlarmTrigger::class.java, JOB_ID, work)
        }
    }

    private var manager: NotificationManager? = null

    override fun onHandleWork(intent: Intent) {
        val action = intent.action
        if (ACTION_ALARM == action) {
            createNotification()
        }
    }

    private fun createNotification() {
        val sleepIntent = Intent(this, SleepReceiver::class.java).apply {
            action = ACTION_SLEEP
        }
        val sleepPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            sleepIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val channelId = resources.getString(R.string.notification_channel)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle(getString(R.string.alarm_title))
            .setContentText(getString(R.string.alarm_text))
            .setSound(defaultSoundUri)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(
                android.R.drawable.ic_notification_overlay,
                resources.getString(R.string.sleep),
                sleepPendingIntent
            )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager == null) {
                manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            if (manager != null && manager!!.getNotificationChannel(channelId) == null) {
                val channel =
                    NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
                manager?.createNotificationChannel(channel)
            }
            manager?.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
        applicationContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit().clear().apply()
        UpdateTimer.updateTimer(applicationContext, null)
    }
}