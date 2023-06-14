package com.example.takenotes

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed ({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },4000)

        val t = AnimationUtils.loadAnimation(this, R.anim.t)
        val b = AnimationUtils.loadAnimation(this, R.anim.b)
        val imageView1 = findViewById<ImageView>(R.id.imageView1)
        val imageView2 = findViewById<ImageView>(R.id.imageView2)

        imageView1.startAnimation(t)
        imageView2.startAnimation(b)

    }
}