package com.example.drinkwater

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.util.Log
import android.widget.Toast
import java.time.LocalDateTime


class ResetAlarm: BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        System.out.println("action:" + intent.action)
        if (intent.action == "com.example.drinkwater.broadcast.reset") {
            Log.d("Alarm Bell", "reset to 0")
            val myDataStore = DataStoreProvider(context)
            System.out.println(LocalDateTime.now())
            myDataStore.updateTotalAmount(0)
        } else {
            Log.d("Alarm Bell", "notify")
            Toast.makeText(context, "Alarm sound", Toast.LENGTH_SHORT).show()

        }

    }

}