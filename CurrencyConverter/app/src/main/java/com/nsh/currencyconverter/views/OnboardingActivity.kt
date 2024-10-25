package com.nsh.currencyconverter.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.nsh.currencyconverter.R
import com.nsh.currencyconverter.views.adapters.OnboardingViewPagerAdapter
import me.relex.circleindicator.CircleIndicator3

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnSkip: Button
    private lateinit var btnNext: Button
    private lateinit var btnStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean("is_first_launch", true)

        if (!isFirstLaunch) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initOnboarding()
        handleEventListener()
    }

    private fun initOnboarding() {
        val circleIndicator: CircleIndicator3 = findViewById(R.id.circleIndicator)
        viewPager = findViewById(R.id.viewPager)
        btnSkip = findViewById(R.id.btnSkip)
        btnNext = findViewById(R.id.btnNext)
        btnStart = findViewById(R.id.btnStart)

        val viewPagerAdapter = OnboardingViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter
        circleIndicator.setViewPager(viewPager)
    }

    private fun handleEventListener() {
        btnSkip.setOnClickListener {
            viewPager.currentItem = 2
        }

        btnNext.setOnClickListener {
            if (viewPager.currentItem < 2) {
                viewPager.currentItem += 1
            }
        }

        btnStart.setOnClickListener {
            val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("is_first_launch", false).apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 2) {
                    btnSkip.visibility = View.INVISIBLE
                    btnNext.visibility = View.GONE
                    btnStart.visibility = View.VISIBLE
                } else {
                    btnSkip.visibility = View.VISIBLE
                    btnNext.visibility = View.VISIBLE
                    btnStart.visibility = View.GONE
                }
            }
        })
    }
}

