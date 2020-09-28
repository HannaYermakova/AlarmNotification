package by.aermakova.alarmnotification.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.aermakova.alarmnotification.data.Time

class MainViewModel : ViewModel() {

    private val _alarmTime = MutableLiveData<Time>()
    val alarmTime: LiveData<Time>
        get() = _alarmTime

    fun setAlarmTime(time: Time?) {
        _alarmTime.postValue(time)
    }
}