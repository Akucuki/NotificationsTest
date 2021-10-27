package com.pirkovitaliysoft.notificationstest.presentation

interface Notificator {
    fun pushNotificationForFragment(fragmentPosition: Int)
    fun removeNotificationForFragment(fragmentPosition: Int)
}