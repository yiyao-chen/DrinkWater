package com.example.drinkwater.ui.home

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "0"
    }
    val text: LiveData<String> = _text


}