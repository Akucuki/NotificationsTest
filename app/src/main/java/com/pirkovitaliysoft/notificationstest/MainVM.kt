package com.pirkovitaliysoft.notificationstest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainVM(initialValue: Int = 1) : ViewModel() {

    init {
        if(initialValue < 1) throw IllegalArgumentException("Initial value must be greater than 0!")
    }

    val currentCount = MutableLiveData(initialValue)

    fun plusOne(){
        currentCount.value = (currentCount.value ?: 1) + 1
    }

    fun minusOne(){
        var count = currentCount.value ?: 1
        if(count > 1) currentCount.value = --count
    }
}