package com.pirkovitaliysoft.notificationstest.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(private val initialValue: Int) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainVM::class.java)){
            return MainVM(initialValue) as T
        }
        throw IllegalArgumentException("Wrong ViewModel class passed to MainFactory!")
    }
}