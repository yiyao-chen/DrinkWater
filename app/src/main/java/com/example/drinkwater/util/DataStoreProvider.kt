package com.example.drinkwater.util

import android.content.Context
import androidx.datastore.preferences.core.*
import com.example.drinkwater.ui.home.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


class DataStoreProvider(private val context: Context) {

    private val TOTAL_AMOUNT = intPreferencesKey("total_amount")
    private val dailyGoal = doublePreferencesKey("daily_goal")
    private val NOTIFICATION_INTERVAL = intPreferencesKey("notification_interval")

    fun updateTotalAmount(value: Int) {
        runBlocking {
            context.dataStore.edit { setting ->
                setting[TOTAL_AMOUNT] = value
            }
        }
    }

    fun updateDailyGoal(value: Double) {
        runBlocking {
            context.dataStore.edit { setting ->
                setting[dailyGoal] = value
            }
        }
    }

    fun updateNotificationInterval(value: Int) {
        runBlocking {
            context.dataStore.edit { setting ->
                setting[NOTIFICATION_INTERVAL] = value
            }
        }
    }

    fun getTotalAmount(): Int? {
        return runBlocking {
            context.dataStore.data.map {
                it[TOTAL_AMOUNT]
            }.first()
        }
    }


    fun getDailyGoal(): Double? {
        return runBlocking {
            context.dataStore.data.map {
                it[dailyGoal]
            }.first()
        }
    }

    fun getNotificationInterval(): Int? {
        return runBlocking {
            context.dataStore.data.map {
                it[NOTIFICATION_INTERVAL]
            }.first()
        }
    }


}