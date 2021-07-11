package com.pirkovitaliysoft.notificationstest

import android.content.Context
import android.content.SharedPreferences

const val BUNDLE_FRAGMENTS_COUNT = "bundle fragments count"
class NotificationstestPreferences(context: Context) {
    private val sharedPref = context.getSharedPreferences(
        context.getString(R.string.app_preferences),
        Context.MODE_PRIVATE
    )
    fun setFragmentsCount(count: Int) {
        sharedPref.putInt(BUNDLE_FRAGMENTS_COUNT, count)
    }
    fun getFragmentsCount() = sharedPref.getInt(BUNDLE_FRAGMENTS_COUNT, 1)
}

private fun SharedPreferences.putInt(key: String, data: Int) {
    edit().putInt(key, data)
        .apply()
}