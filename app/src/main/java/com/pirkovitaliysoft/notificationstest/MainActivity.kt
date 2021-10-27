package com.pirkovitaliysoft.notificationstest

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.pirkovitaliysoft.notificationstest.adapters.FragmentsViewPagerAdapter
import com.pirkovitaliysoft.notificationstest.presentation.viewModel.MainVM
import com.pirkovitaliysoft.notificationstest.presentation.viewModel.MainViewModelFactory
import com.pirkovitaliysoft.notificationstest.databinding.ActivityMainBinding
import com.pirkovitaliysoft.notificationstest.presentation.Notificator
import com.pirkovitaliysoft.notificationstest.utils.FragmentOpeningNotificationsGenerator

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

        initializeViewModel(initialCount)
        initializeViewPagerPreviousPosition()

        vm.currentCount.observe(this) { viewModelItemCount ->
            val adapterItemCount = viewPagerAdapter.itemCount
            synchronizeActualPosition(adapterItemCount, viewModelItemCount)
        }
    }

    private fun initializeViewModel(initialCount: Int) {
        val factory = MainViewModelFactory(initialCount)
        vm = ViewModelProvider(this, factory).get(MainVM::class.java)
    }

    /*
           Checks what the action to perform on
           value in the adapter according to
           value in the ViewModel to equalize them.

           Probably not the best solution, but I didn't found better
        */
    private fun synchronizeActualPosition(
        adapterItemCount: Int,
        viewModelItemCount: Int
    ) {
        when {
            adapterItemCount < viewModelItemCount -> {
                viewPagerAdapter.addLastFragment()
                binding.viewPager.setCurrentItem(viewModelItemCount, true)
            }
            adapterItemCount > viewModelItemCount -> {
                viewPagerAdapter.removeLastFragment()

                val removedFragmentActualPosition = viewModelItemCount - 1
                removeNotificationForFragment(removedFragmentActualPosition)
            }
        }
    }

    private fun initializeViewPagerPreviousPosition() {
        intent.extras?.getInt(FRAGMENT_POSITION, 0)?.let {
            if (viewPagerAdapter.itemCount >= it) binding.viewPager.currentItem = it
        }
    }

    override fun onBackPressed() {
        binding.viewPager.currentItem.let { currentPosition ->
            if(currentPosition == 0){
                super.onBackPressed()
            }else {
                moveViewPagerOnePositionBack(currentPosition)
            }
        }
    }

    private fun moveViewPagerOnePositionBack(previousPosition: Int) {
        val newPosition = previousPosition - 1
        binding.viewPager.setCurrentItem(newPosition, true)
    }

    override fun onStop() {
        prefs.setFragmentsCount(viewPagerAdapter.itemCount)
        super.onStop()
    }

    override fun pushNotificationForFragment(fragmentPosition: Int) {
        FragmentOpeningNotificationsGenerator.generateNotificationForFragment(this, fragmentPosition)
    }

    override fun removeNotificationForFragment(fragmentPosition: Int) {
        FragmentOpeningNotificationsGenerator.removeNotificationForFragment(this, fragmentPosition)
    }
}