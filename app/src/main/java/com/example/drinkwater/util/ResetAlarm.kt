package com.example.drinkwater.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.drinkwater.util.DataStoreProvider


class ResetAlarm: BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        val myDataStore = DataStoreProvider(context)
        myDataStore.updateTotalAmount(0)

    }

}