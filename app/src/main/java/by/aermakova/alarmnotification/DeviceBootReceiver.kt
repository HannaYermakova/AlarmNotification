package by.aermakova.alarmnotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import by.aermakova.alarmnotification.util.Constants.BOOT_COMPLETED

class DeviceBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && context != null) {
            if (BOOT_COMPLETED == intent.action) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(Intent(context, SetAlarmService::class.java))
                } else context.startService(Intent(context, SetAlarmService::class.java))
            }
        }
    }
}