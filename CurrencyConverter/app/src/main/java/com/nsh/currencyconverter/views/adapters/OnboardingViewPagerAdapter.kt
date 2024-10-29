package com.nsh.currencyconverter.views.adapters

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nsh.currencyconverter.views.fragments.OnboardingFragment1
import com.nsh.currencyconverter.views.fragments.OnboardingFragment2

class OnboardingViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    @NonNull
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> OnboardingFragment2()
            else -> OnboardingFragment1()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}
