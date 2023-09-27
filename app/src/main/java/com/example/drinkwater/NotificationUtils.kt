package com.example.drinkwater

import android.app.Notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.*

object NotificationUtils {
    fun createNotificationChannel(context: Context, channelId: String, channelName: String) {
        System.out.println("createNotificationChannel..")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun buildNotification(context: Context, channelId: String, title: String, content: String): Notification {
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24) // Replace with your notification icon
            .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI) // Add sound
            .build()
    }

    fun scheduleNotification(context: Context, channelId: String, title: String, content: String, intervalMillis: Long) {
        System.out.println("scheduleNotification..")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(context, NotificationBroadcastReceiver::class.java)
        notificationIntent.putExtra("channelId", channelId)
        notificationIntent.putExtra("title", title)
        notificationIntent.putExtra("content", content)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, intervalMillis.toInt())

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            intervalMillis,
            pendingIntent
        )
    }
}