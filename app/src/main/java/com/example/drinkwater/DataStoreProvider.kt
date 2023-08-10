package com.example.drinkwater

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.drinkwater.ui.home.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


class DataStoreProvider(private val context: Context) {

    private val TOTAL_AMOUNT = intPreferencesKey("total_amount")

//    suspend fun readTotalAmount(): Int? {
//        var totalAmount: Int? = null
//        context.dataStore.data
//            .map { preferences ->
//                totalAmount = preferences[TOTAL_AMOUNT]
//            }
//        return totalAmount
//
//        return context.dataStore.data
//            .map { preferences -> preferences[TOTAL_AMOUNT] ?: null }
//            .first()
//    }

    suspend fun updateTotalAmount(totalAmount: Int) {

        context.dataStore.edit { setting ->
            setting[TOTAL_AMOUNT] = totalAmount
        }
    }

    fun getTotalAmount(): Int? {

        return runBlocking {
            context.dataStore.data.map {
                it[TOTAL_AMOUNT] ?: null
            }.first()
        }
    }

    fun <T> readNonNullData(key: Preferences.Key<T>, defValue: T): T {

        return runBlocking {
            context.dataStore.data.map {
                it[key] ?: defValue
            }.first()
        }
    }
}