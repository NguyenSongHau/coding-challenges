package com.nsh.currencyconverter.views

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nsh.currencyconverter.R
import com.nsh.currencyconverter.views.fragments.HomeFragment
import com.nsh.currencyconverter.views.fragments.ChartFragment

class BottomNavigationActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private val homeFragment = HomeFragment()
    private val chartFragment = ChartFragment()

    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(R.layout.activity_bottom_navigation)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bottomNavigationScreen)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomNavigationView = findViewById(R.id.bottomNavigation)

        supportFragmentManager.beginTransaction().add(R.id.frameContainer, chartFragment, "2").hide(chartFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.frameContainer, homeFragment, "1").commit()

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit()
                    activeFragment = homeFragment
                    true
                }
                R.id.bottom_chart -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(chartFragment).commit()
                    activeFragment = chartFragment
                    true
                }
                else -> false
            }
        }
    }
}

