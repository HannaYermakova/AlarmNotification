package by.aermakova.alarmnotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import by.aermakova.alarmnotification.util.Constants.ACTION_ALARM

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, int: Intent) {
        Log.d("A_AlarmReceiver", "onReceive")
        val action = int.action
        if (ACTION_ALARM == action) {
            createAlarm(context)
        }
    }

    private fun createAlarm(context: Context) {
        val intent = Intent(context, AlarmTrigger::class.java).apply {
            action = ACTION_ALARM
        }
        AlarmTrigger.enqueueWork(context, intent)
    }
}