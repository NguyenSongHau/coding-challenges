package com.nsh.currencyconverter.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nsh.currencyconverter.R

/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingFragment3.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnboardingFragment3 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_onboarding_3, container, false)
    }
}