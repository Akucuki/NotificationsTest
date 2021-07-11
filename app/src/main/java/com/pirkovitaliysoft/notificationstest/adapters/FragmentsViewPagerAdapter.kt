package com.pirkovitaliysoft.notificationstest.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pirkovitaliysoft.notificationstest.presentation.TemplateFragment

class FragmentsViewPagerAdapter(fragmentActivity: FragmentActivity, initialCount: Int = 1) : FragmentStateAdapter(fragmentActivity) {
    private var fragmentsCount = initialCount

    override fun getItemCount() = fragmentsCount

    override fun createFragment(position: Int): Fragment {
        return TemplateFragment.newInstance(position)
    }

    fun addLastFragment(){
        notifyItemInserted(++fragmentsCount)
    }

    fun removeLastFragment(){
        if(fragmentsCount > 1) notifyItemRemoved(--fragmentsCount)
    }
}