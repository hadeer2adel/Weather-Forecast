package com.example.weatherforecast.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherforecast.R

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var lottie_1: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        lottie_1 = findViewById(R.id.lottie_1)

        Handler().postDelayed( {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
        }, 4000)
    }
}