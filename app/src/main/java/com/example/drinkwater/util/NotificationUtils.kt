package com.example.drinkwater.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock

object NotificationUtils {
    fun setAlarm(context: Context, timeInMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ResetAlarm::class.java)
        intent.setAction("com.example.drinkwater.broadcast.reset")
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.setRepeating(
            AlarmManager.RTC,
            timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun setNotificationAlarm(context: Context, intervalTimeInMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        intent.setAction("com.example.drinkwater.broadcast.notify")
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(),
            intervalTimeInMillis,
            pendingIntent
        )
    }

}