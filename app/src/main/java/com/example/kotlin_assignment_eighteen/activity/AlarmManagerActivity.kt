package com.example.kotlin_assignment_eighteen.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlin_assignment_eighteen.const.Constants.Companion.TIME_PICKER_ONCE_TAG
import com.example.kotlin_assignment_eighteen.databinding.ActivityAlarmManagerBinding
import com.example.kotlin_assignment_eighteen.fragment.TimePickerFragment
import com.example.kotlin_assignment_eighteen.service.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*

class AlarmManagerActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var binding: ActivityAlarmManagerBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectTime.setOnClickListener { onClickBtnSelectTime() }
        binding.btnSetAlarm.setOnClickListener { onClickBtnSetAlarm() }
        binding.btnCancelAlarm.setOnClickListener { onClickBtnCancelAlarm() }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun onClickBtnCancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.cancel(pendingIntent)

        Toast.makeText(this, "Alarm succesfully canceled", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun onClickBtnSetAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(this, "Repeating alarm succesfully set up", Toast.LENGTH_SHORT).show()
    }

    private fun onClickBtnSelectTime() {
        val timePickerFragmentOne = TimePickerFragment()
        timePickerFragmentOne.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        // Siapkan time formatter-nya terlebih dahulu
        calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        // Set text dari textview berdasarkan tag
        when (tag) {
            TIME_PICKER_ONCE_TAG -> binding.tvTimeAlarm.text = dateFormat.format(calendar.time)
            else -> {
            }
        }
    }
}