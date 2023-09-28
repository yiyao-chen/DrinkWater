package com.example.drinkwater.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.drinkwater.R


class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        System.out.println("NotificationBroadcastReceiver receive")
        showNotifications(context)
    }

    fun showNotifications(context: Context) {
        val channelId = "channel_id"
        val title = "Hey " + "\uD83D\uDC4B"
        val contentText = "It's time to drink some water! " + "\uD83D\uDCA7"

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, builder.build())
    }
}