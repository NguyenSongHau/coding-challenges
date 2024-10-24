package com.nsh.currencyconverter.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.nsh.currencyconverter.R

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        val splashView = findViewById<ConstraintLayout>(R.id.splashScreen)
        val splashImage = findViewById<ImageView>(R.id.splashImage)

        splashView.alpha = 0f
        splashImage.alpha = 0f

        splashView.animate()
            .alpha(1f)
            .setDuration(1000)
            .withEndAction {
                splashImage.animate()
                    .alpha(1f)
                    .setDuration(1500)
                    .withEndAction {
                        Handler(Looper.getMainLooper()).postDelayed({
                            splashImage.animate()
                                .alpha(0f)
                                .setDuration(1000)
                                .withEndAction {
                                    val intent = Intent(this, OnboardingActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        }, SPLASH_DELAY)
                    }
            }
    }
}
