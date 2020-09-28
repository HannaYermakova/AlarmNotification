package by.aermakova.alarmnotification.ui

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePicker : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(
            requireContext(), requireActivity() as? TimePickerDialog.OnTimeSetListener?,
            hour,
            minute,
            DateFormat.is24HourFormat(requireContext())
        )
    }
}