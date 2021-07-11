package com.pirkovitaliysoft.notificationstest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.pirkovitaliysoft.notificationstest.adapters.FragmentsViewPagerAdapter
import com.pirkovitaliysoft.notificationstest.databinding.ActivityMainBinding
import com.pirkovitaliysoft.notificationstest.presentation.Notificator
import com.pirkovitaliysoft.notificationstest.utils.NotificationsHelper

const val FRAGMENT_POSITION = "fragmentPosition"

class MainActivity : FragmentActivity(), Notificator {

    private lateinit var binding: ActivityMainBinding
    private lateinit var vm: MainVM
    private lateinit var viewPagerAdapter: FragmentsViewPagerAdapter
    private lateinit var prefs: NotificationstestPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = NotificationstestPreferences(this)
        val initialCount = prefs.getFragmentsCount()

        viewPagerAdapter = FragmentsViewPagerAdapter(this, initialCount)
        binding.viewPager.adapter = viewPagerAdapter

        intent.extras?.getInt(FRAGMENT_POSITION, 0)?.let {
            if(viewPagerAdapter.itemCount >= it) binding.viewPager.currentItem = it
        }

        val factory = MainViewModelFactory(initialCount)
        vm = ViewModelProvider(this, factory).get(MainVM::class.java)

        /*
            Checks what the action to perform on
            value in the adapter according to
            value in the ViewModel.

            Probably not the best solution, but I didn't found better
         */
        vm.currentCount.observe(this) { viewModelCount ->
            val adapterCount = viewPagerAdapter.itemCount
            when {
                adapterCount < viewModelCount -> viewPagerAdapter.addLastFragment()
                adapterCount > viewModelCount -> viewPagerAdapter.removeLastFragment()
            }
        }
    }

    override fun onBackPressed() {
        binding.viewPager.currentItem.let { currentItem ->
            if(currentItem == 0){
                super.onBackPressed()
            }else {
                binding.viewPager.currentItem = currentItem - 1
            }
        }
    }

    override fun onStop() {
        prefs.setFragmentsCount(viewPagerAdapter.itemCount)
        super.onStop()
    }

    override fun pushNotificationForFragment(fragmentPosition: Int) {
        NotificationsHelper.generateNotificationForFragment(this, fragmentPosition)
    }
}