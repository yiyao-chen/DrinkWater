package com.example.drinkwater.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val itemLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private val _text = MutableLiveData<String>().apply {
        value = "Set alarms:"
    }
    val text: LiveData<String> = _text


    fun changeData(infoStr: String) {
        itemLiveData.value = infoStr
        System.out.println("changed itemLiveData: " + itemLiveData.value)

    }

}