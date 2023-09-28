package com.example.drinkwater

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        System.out.println("NotificationBroadcastReceiver action:" + intent.action)
        Toast.makeText(context, "notify....", Toast.LENGTH_LONG).show()

        showNotifications(context)
    }

    fun showNotifications(context: Context) {
        val channelId = "your_channel_id"
        val title = "Hello"
        val contentText = "It's time to drink some water!"

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, builder.build())
    }
}