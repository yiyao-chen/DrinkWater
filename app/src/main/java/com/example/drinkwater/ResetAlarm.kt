package com.example.drinkwater

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.drinkwater.ui.home.dataStore
import java.time.LocalDateTime

class ResetAlarm: BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        Log.d("Alarm Bell", "reset to 0")
        val myDataStore = DataStoreProvider(context)
        System.out.println(LocalDateTime.now())
        myDataStore.updateTotalAmount(0)
    }

}