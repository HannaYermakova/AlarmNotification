package by.aermakova.alarmnotification.ui

import android.app.TimePickerDialog
import android.content.*
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import by.aermakova.alarmnotification.CancelAlarmService
import by.aermakova.alarmnotification.R
import by.aermakova.alarmnotification.SetAlarmService
import by.aermakova.alarmnotification.data.Time
import by.aermakova.alarmnotification.databinding.ActivityMainBinding
import by.aermakova.alarmnotification.util.Constants.ALARM_TIME_PREF
import by.aermakova.alarmnotification.util.Constants.PREFERENCES
import by.aermakova.alarmnotification.util.Constants.UPDATE_UI
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener,
    UpdateTimerListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var preferences: SharedPreferences
    private lateinit var broadcastReceiver: UpdateTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        preferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        broadcastReceiver = UpdateTimer(this)
        broadcastReceiver.registerUpdateTimer(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.timeText.setOnClickListener {
            val timePicker: DialogFragment =
                TimePicker()
            timePicker.show(supportFragmentManager,
                TIME_PICKER_TAG
            )
        }

        binding.stopButton.setOnClickListener {
            startService(Intent(this, CancelAlarmService::class.java))
            preferences.edit().clear().apply()
            viewModel.setAlarmTime(null)
        }
    }

    override fun onResume() {
        super.onResume()
        val timeText = preferences.getString(ALARM_TIME_PREF, null)
        if (timeText != null) {
            val time = Gson().fromJson(timeText, Time::class.java)
            viewModel.setAlarmTime(time)
        }
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val time = Time(
            hourOfDay,
            minute,
            DateFormat.is24HourFormat(this)
        )
        viewModel.setAlarmTime(time)
        val timeText = Gson().toJson(time)
        preferences.edit().putString(ALARM_TIME_PREF, timeText).apply()
        startService(Intent(this, SetAlarmService::class.java))
    }

    companion object {
        private const val TIME_PICKER_TAG = "alarm_time_picker"
    }

    override fun onDestroy() {
        broadcastReceiver.unregisterUpdateTimer(this)
        super.onDestroy()
    }

    override fun updateTimer(newTime: String?) {
        if (newTime == null) {
            viewModel.setAlarmTime(null)
        } else {
            val time = Gson().fromJson<Time>(newTime, Time::class.java)
            viewModel.setAlarmTime(time)
        }
    }
}

interface UpdateTimerListener {
    fun updateTimer(newTime: String?)
}


class UpdateTimer(private val listener: UpdateTimerListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val newTime = intent?.getStringExtra(NEW_TIME)
        listener.updateTimer(newTime)
    }

    companion object {
        private const val NEW_TIME = "by.aermakova.alarmnotification.new_time"
        fun updateTimer(context: Context, newTime: String?) {
            val intent = Intent(UPDATE_UI).apply {
                putExtra(NEW_TIME, newTime)
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }

    fun registerUpdateTimer(context: Context) {
        val filter = IntentFilter(UPDATE_UI)
        LocalBroadcastManager.getInstance(context).registerReceiver(this, filter)
    }

    fun unregisterUpdateTimer(context: Context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
    }
}