package com.example.drinkwater

import android.content.Context
import androidx.datastore.preferences.core.*
import com.example.drinkwater.ui.home.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


class DataStoreProvider(private val context: Context) {

    private val TOTAL_AMOUNT = intPreferencesKey("total_amount")
    private val dailyGoal = doublePreferencesKey("daily_goal")
    private val switch08 = booleanPreferencesKey("switch08")
    private val switch10 = booleanPreferencesKey("switch10")

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

    fun updateSwitchState(switch: String, value: Boolean) {
        val switch = booleanPreferencesKey("switchName")

        runBlocking {
            context.dataStore.edit { setting ->
                setting[switch] = value
            }
        }
    }

    fun getSwitchState(switchName: String): Boolean? {
        val switch = booleanPreferencesKey("switchName")

        return runBlocking {
            context.dataStore.data.map {
                it[switch]
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