package com.example.drinkwater

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.drinkwater.ui.home.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


class DataStoreProvider(private val context: Context) {

    private val TOTAL_AMOUNT = intPreferencesKey("total_amount")
    private val dailyGoal = doublePreferencesKey("daily_goal")

    fun updateTotalAmount(value: Int) {
        System.out.println("updateTotalAmount val " + value)
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

    fun <T> readIntData(key: Preferences.Key<T>, defValue: T): T {
        return runBlocking {
            context.dataStore.data.map {
                it[key] ?: defValue
            }.first()
        }
    }


}