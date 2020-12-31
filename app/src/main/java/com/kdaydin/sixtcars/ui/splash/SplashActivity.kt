package com.kdaydin.sixtcars.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kdaydin.sixtcars.ui.map.MapsActivity

class SplashActivity : AppCompatActivity() {
    val viewModel by lazy<SplashViewModel> {
        SplashViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MapsActivity::class.java))
        finish()
    }

}