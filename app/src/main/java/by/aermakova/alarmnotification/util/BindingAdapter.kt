package by.aermakova.alarmnotification.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import by.aermakova.alarmnotification.R
import by.aermakova.alarmnotification.data.Time

object BindingAdapters {

    @BindingAdapter("app:set_time")
    @JvmStatic
    fun convertTime(textView: TextView, selectedTime: Time?) {
        if (selectedTime != null) {
            with(selectedTime) {
                val h = if (!is24HourFormat) {
                    when (hours) {
                        in 0..12 -> hours
                        else -> hours - 12
                    }
                } else hours
                val m = if (minutes < 10) "0$minutes" else "$minutes"
                var time = "$h:$m"
                if (!is24HourFormat) {
                    time += when (hours) {
                        in 0..12 -> " AM"
                        else -> " PM"
                    }
                }
                textView.text = time
            }
        } else textView.text = textView.context.resources.getText(R.string.select)
    }
}